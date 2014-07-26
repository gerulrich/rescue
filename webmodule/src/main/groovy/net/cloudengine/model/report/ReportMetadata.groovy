package net.cloudengine.model.report

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="report_metadata")
class ReportMetadata {
	
	@Id ObjectId id;
	String name;
	String category;
	String datasource;
	String fileid;
	List<ReportParameter> parameters;
}
