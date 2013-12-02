package net.cloudengine.reports

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

//@Indexes(@Index(value='date', expireAfterSeconds=360, name='date_index'))
@Document(collection='report_token')
class ReportExecution {
	
	@Id ObjectId id;
	String status;
	String error;
	Date date;
	byte[] report;
	String username;
	String fileName;

}
