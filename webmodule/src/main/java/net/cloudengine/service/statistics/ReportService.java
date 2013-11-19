package net.cloudengine.service.statistics;

import java.util.Collection;
import java.util.Map;

import org.bson.types.ObjectId;

import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.reports.ReportExecution;
import net.cloudengine.reports.ReportMetadata;

public interface ReportService {
	
	ReportMetadata getReportMetadata(FileDescriptor descriptor) throws ReportException;
	
	ReportExecution getReportExecution(ObjectId id);
	
	byte[] executeReport(FileDescriptor descriptor, Map<String, Object> params) throws ReportException;
	
	Collection<ReportExecution> getReportsByUsername(String username);

}
