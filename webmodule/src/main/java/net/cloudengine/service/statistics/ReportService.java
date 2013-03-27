package net.cloudengine.service.statistics;

import java.util.Map;

import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.reports.ReportMetadata;

public interface ReportService {
	
	ReportMetadata getReportMetadata(FileDescriptor descriptor) throws ReportException;
	
	byte[] executeReport(FileDescriptor descriptor, Map<String, Object> params) throws ReportException;

}
