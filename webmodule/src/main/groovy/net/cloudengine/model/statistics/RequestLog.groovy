package net.cloudengine.model.statistics

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("request_log")
class RequestLog {
	
	@Id ObjectId id;
	Date time;

}
