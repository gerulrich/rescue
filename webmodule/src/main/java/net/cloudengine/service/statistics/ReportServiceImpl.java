package net.cloudengine.service.statistics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import net.cloudengine.api.BlobStore;
import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.api.mongo.MongoDBWrapper;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.reports.ReportExecution;
import net.cloudengine.reports.ReportMetadata;
import net.cloudengine.reports.ReportMetadataDSL;
import net.cloudengine.util.ZipUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.jaspersoft.mongodb.connection.MongoDbConnection;

@Service
public class ReportServiceImpl implements ReportService {

	private static final String DATASOURCE_MONGODB = "MongoDB";
	private static final String DATASOURCE = "datasource";
	private BlobStore blobStore;
	private JdbcTemplate template;
	private MongoDBWrapper mongoDBWrapper;
	private Datastore<ReportExecution, ObjectId> reportFileDatastore;
	
	@Autowired
	public ReportServiceImpl(BlobStore blobStore, JdbcTemplate template, MongoDBWrapper mongoDBWrapper,
			@Qualifier("reportExecution") Datastore<ReportExecution, ObjectId> reportFileDatastore) {
		super();
		this.blobStore = blobStore;
		this.template = template;
		this.mongoDBWrapper = mongoDBWrapper;
		this.reportFileDatastore = reportFileDatastore;
	}

	@Override
	public ReportMetadata getReportMetadata(FileDescriptor descriptor) throws ReportException {
		InputStream inputStream = blobStore.retrieveFile(descriptor.getFileObjectId());
		String files[] = ZipUtils.unzipp(inputStream);
		String manifestFileName = ZipUtils.findByName(files, "manifest.groovy");
		
		if (manifestFileName == null) {
			ZipUtils.deleteFiles(files);
			throw new ReportException("No se encontró el archivo manifest.groovy en el reporte");
		}
		
		ReportMetadata rmd = null;
		
		try {
			ReportMetadataDSL dslParser = new ReportMetadataDSL();
			rmd = dslParser.parseDSL(new File(manifestFileName));
		} catch (Exception e) {
			throw new ReportException(e);
		} finally {
			ZipUtils.deleteFiles(files);
		}
		
		return rmd;
	}
	
	
	public byte[] executeReport(FileDescriptor descriptor, Map<String, Object> params) throws ReportException {
		
		String files[] = null;
		Connection connection = null;
		MongoDbConnection mongoConnection = null;
		String datasource = null;
		
		if (params.containsKey(DATASOURCE)) {
			datasource = params.get(DATASOURCE).toString();
		}
		
		try {
			InputStream inputStream = blobStore.retrieveFile(descriptor.getFileObjectId());
			files = ZipUtils.unzipp(inputStream);
			String reportFileName = ZipUtils.findByName(files, "report.jasper");
			if (reportFileName == null) {
				ZipUtils.deleteFiles(files);
				throw new ReportException("No se encontró el archivo report.jasper en el reporte");
			}
			
			if (DATASOURCE_MONGODB.equalsIgnoreCase(datasource)) {
				mongoConnection = createMongoDbConnection();
			} else {
				connection = DataSourceUtils.getConnection(template.getDataSource());
			}
			
			JasperPrint jp = JasperFillManager.fillReport(reportFileName, params, connection != null ? connection : mongoConnection);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

			exporter.exportReport();

			return baos.toByteArray();
			
		} catch (Exception e) {
			throw new ReportException(e);
		} finally {
			if (files != null) {
				ZipUtils.deleteFiles(files);
			}
			if (connection != null) {
				DataSourceUtils.releaseConnection(connection, template.getDataSource());
			}
			if (mongoConnection != null) {
				mongoConnection.close();
			}
		}
	}
	
	@Override
	public ReportExecution getReportExecution(ObjectId id) {
		return reportFileDatastore.get(id);
	}

	@Override
	public Collection<ReportExecution> getReportsByUsername(String username) {
		Query<ReportExecution> query = reportFileDatastore.createQuery();
		query.field("username").eq(username).field("status").eq("OK");
		return query.list();
	}
	
	private MongoDbConnection createMongoDbConnection() throws JRException {
		return new MongoDbConnection(mongoDBWrapper.getURI(), mongoDBWrapper.getUser(), mongoDBWrapper.getPass());
	}
	
	
}
