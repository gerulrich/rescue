package net.cloudengine.model.statistics

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="request_log")
class RequestLog {
	
	@Id ObjectId id;
	Date time;
	long executionTime;
	String url;
	String controller;
	String method;
	String user;
	String status;
	String[] errors;
}
