package net.cloudengine.service;

import java.util.Collection;
import java.util.Map;

import net.cloudengine.dao.support.Page;
import net.cloudengine.model.report.Report;
import net.cloudengine.model.report.ReportMetadata;
import net.cloudengine.model.report.ReportParameter;

import org.bson.types.ObjectId;

public interface ReportService {
	
	Page<ReportMetadata> getReportsMetadata(int page, int size);
	ReportMetadata getReportMetadata(ObjectId id);
	void deleteReportsMetadata(ObjectId key);
	Collection<ReportParameter> getValidParametersForCurrentUser(ReportMetadata reportMetadata);

	Report createReport(ObjectId reportMetadataId, Map<String,String> parameters);
	
	Report getReport(ObjectId id);
	Page<Report> getReportsForCurrentUser(int page, int size);
	Collection<Report> getReportsByUsername(String username);
	void deleteReports(ObjectId key);
	
}
