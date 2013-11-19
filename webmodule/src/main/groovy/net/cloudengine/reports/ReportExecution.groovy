package net.cloudengine.reports

import org.bson.types.ObjectId

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Index
import com.google.code.morphia.annotations.Indexes

@Entity(value='report_token', noClassnameStored=true)
@Indexes(@Index(value='date', expireAfterSeconds=360, name='date_index'))
class ReportExecution {
	
	@Id ObjectId id;
	String status;
	String error;
	Date date;
	byte[] report;
	String username;
	String fileName;

}
