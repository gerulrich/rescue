package net.cloudengine.api.mongo.dao;

import java.util.List;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.statistics.RequestLog;

import org.bson.types.ObjectId;

public interface RequestLogDao extends Datastore<RequestLog, ObjectId> {
	
	List<RequestLog> getRequestLog(String controller, String method);
	
}
