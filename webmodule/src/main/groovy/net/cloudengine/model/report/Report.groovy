package net.cloudengine.model.report

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection='report')
class Report {
	
	@Id ObjectId id;
	String name;
	String title;
	String owner;
	Date date;
	byte[] data;
	Map<String,Object> parameters;
	List<String> messages;
	ReportStatus status;	

}
