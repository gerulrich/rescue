package net.cloudengine.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.cloudengine.dao.support.Page;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.dao.support.SearchParametersBuilder;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.report.Report;
import net.cloudengine.model.report.ReportMetadata;
import net.cloudengine.model.report.ReportParameter;
import net.cloudengine.service.ReportService;
import net.cloudengine.service.SessionService;
import net.cloudengine.service.amqp.AMQPService;
import net.cloudengine.service.amqp.EndPoint;

import org.bson.types.ObjectId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class ReportServiceImpl implements ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
	private Repository<Report, ObjectId> reportRepository;
	private Repository<ReportMetadata, ObjectId> reportMetadataRepository;
	private AMQPService amqpPService;
	private SessionService sessionService;
	
	@Autowired
	public ReportServiceImpl(AMQPService amqpPService, SessionService sessionService,
			RepositoryLocator repositoryLocator) {
		super();
		this.amqpPService = amqpPService;
		this.sessionService = sessionService;
		this.reportRepository = repositoryLocator.getRepository(Report.class);
		this.reportMetadataRepository = repositoryLocator.getRepository(ReportMetadata.class);
	}

	@Override
	public Page<ReportMetadata> getReportsMetadata(int page, int size) {
		return reportMetadataRepository.list(page, size);
	}

	@Override
	public ReportMetadata getReportMetadata(ObjectId id) {
		return reportMetadataRepository.get(id);
	}

	@Override
	public Report getReport(ObjectId id) {
		User currentUser = sessionService.getCurrentUser();
		Report report = reportRepository.get(id);
		if (report != null && report.getOwner().equals(currentUser.getUsername())) {
			return report;
		}
		return null;
	}
	
	@Override
	public void deleteReportsMetadata(ObjectId key) {
		reportMetadataRepository.delete(key);
	}

	@Override
	public void deleteReports(ObjectId key) {
		reportRepository.delete(key);
	}

	@Override
	public Page<Report> getReportsForCurrentUser(int page, int size) {
		SearchParametersBuilder builder = SearchParametersBuilder.forClass(Report.class);
		builder.eq("owner", sessionService.getCurrentUser().getUsername());
		return reportRepository.find(builder.build(), page, size);
	}

	@Override
	public Collection<Report> getReportsByUsername(String username) {
		SearchParametersBuilder builder = SearchParametersBuilder.forClass(Report.class);
		builder.eq("owner", username);
		return reportRepository.findAll(builder.build());
	}
	
	@Override
	public Collection<ReportParameter> getValidParametersForCurrentUser(ReportMetadata reportMetadata) {
		List<ReportParameter> params = Lists.newArrayList();
		for (ReportParameter param : reportMetadata.getParameters()) {
			if (!param.isVisible() && param.hasPermission(sessionService.getCurrentUser())) {
				params.add(param);
			}
		}
		return params;
	}
	
	public Report createReport(ObjectId reportMetadataId, Map<String,String> parameters) {
		ReportMetadata reportMetadata = this.getReportMetadata(reportMetadataId);
		Report report = new Report();
		report.setOwner(sessionService.getCurrentUser().getUsername());
		report.setTitle(reportMetadata.getName());
		report.setDate(new Date());
		report.setParameters(prepareParametersForSave(reportMetadata, parameters));
		reportRepository.save(report);
		this.sendReportJobToQueue(reportMetadataId, report.getId().toString(), parameters);
		return report;
	}
	
	private Map<String, Object> prepareParametersForSave(ReportMetadata reportMetadata, Map<String, String> parameters) {
		Map<String, Object> result = Maps.newHashMap();
		Map<String,ReportParameter> reportParameters = Maps.newHashMap();
		for(ReportParameter parameter : reportMetadata.getParameters()) {
			reportParameters.put(parameter.getName(), parameter);
		}
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			ReportParameter parameter = reportParameters.get(entry.getKey());
			if (parameter != null) {
				result.put(parameter.getLabel(), entry.getValue());
			} else {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	private boolean sendReportJobToQueue(ObjectId id, String token, Map<String,String> parameters) {
		try {
			ReportMetadata report = this.getReportMetadata(id);
			User currentUser = sessionService.getCurrentUser();
			JsonObject jobMessage = new JsonObject();
			jobMessage.add("id", report.getId().toString());
			jobMessage.add("datasource", report.getDatasource());
			jobMessage.add("username", currentUser.getUsername());
			jobMessage.add("token", token);
			jobMessage.add("params", createParameters(currentUser, report, parameters));
			if (logger.isDebugEnabled()) {
				logger.debug("Enviando mensaje: {}", jobMessage.toString());
			}
			amqpPService.send(jobMessage.toString(), EndPoint.REPORT_EXECUTION);
			return true;
		} catch (Exception e) {
			logger.error("Error to send message to queue", e);
			return false;
		}
	}

	private JsonArray createParameters(User user, ReportMetadata reportMetadata, Map<String, String> parameters) {
		JsonArray parametersJson = new JsonArray();
		
		String value = null;
		for (ReportParameter param : reportMetadata.getParameters()) {
			if (!param.isVisible() || !param.hasPermission(user)) {
				value = transformValue(param, param.getValue());
			} else {
				value = transformValue(param, parameters.get(param.getName()));
			}
			JsonObject paramJson = new JsonObject();
			paramJson.add(param.getName(), value);
			parametersJson.add(paramJson);
		}
		return parametersJson;
	}
	
	/** Transforma los valores en un formato estandar antes de generar el json,
	 * por ejemlo los valores de los campos de fechas que pueden ser diferentes
	 * en la presentacion segun el patron configurado en el reporte. 
	 * @param param
	 * @param value
	 * @return
	 */
	private String transformValue(ReportParameter param, String value) {
		if ("date".equalsIgnoreCase(param.getType()) || "datetime".equalsIgnoreCase(param.getType())) {
			try {
				Date date = new SimpleDateFormat(param.getPattern()).parse(value);
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			} catch (ParseException e) {
				logger.error("Error to parse date {} with pattern {}", value, param.getPattern());
			}
		}
		return value;
	}
}
