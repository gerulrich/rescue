package net.cloudengine.dao.mongodb;

import java.util.List;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.model.statistics.RequestLog;

import org.bson.types.ObjectId;

public interface RequestLogRepository extends Repository<RequestLog, ObjectId> {

	List<RequestLog> getByControllerAndMethod(String controller, String method);

}