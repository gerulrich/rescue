package net.cloudengine.service.statistics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.Map;

import net.cloudengine.api.BlobStore;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.reports.ReportMetadata;
import net.cloudengine.reports.ReportMetadataDSL;
import net.cloudengine.util.ZipUtils;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

	private BlobStore blobStore;
	private JdbcTemplate template;
	
	@Autowired
	public ReportServiceImpl(BlobStore blobStore, JdbcTemplate template) {
		super();
		this.blobStore = blobStore;
		this.template = template;
	}

	@Override
	public ReportMetadata getReportMetadata(FileDescriptor descriptor) throws ReportException {
		
		String files[] = ZipUtils.unzipp(descriptor, blobStore);
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
		try {
			files = ZipUtils.unzipp(descriptor, blobStore);
			String reportFileName = ZipUtils.findByName(files, "report.jasper");
			if (reportFileName == null) {
				ZipUtils.deleteFiles(files);
				throw new ReportException("No se encontró el archivo report.jasper en el reporte");
			}
			connection = DataSourceUtils.getConnection(template.getDataSource());
			JasperPrint jp = JasperFillManager.fillReport(reportFileName, params,connection);
			
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
		}
	}
}
