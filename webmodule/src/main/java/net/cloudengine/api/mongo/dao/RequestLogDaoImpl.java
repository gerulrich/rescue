package net.cloudengine.api.mongo.dao;

import java.util.List;

import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.statistics.RequestLog;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

//import com.google.code.morphia.Datastore;
//import com.google.code.morphia.Morphia;
//import com.google.code.morphia.query.Query;

public class RequestLogDaoImpl extends MongoStore<RequestLog, ObjectId> implements RequestLogDao {

	public RequestLogDaoImpl(MongoTemplate mongoTemplate) {
		super(mongoTemplate, RequestLog.class);
	}

	@Override
	public List<RequestLog> getRequestLog(String controller, String method) {
//		Datastore ds = this.createMorphiaDatastore();
//		Query<RequestLog> q = ds.createQuery(RequestLog.class);
//		q.and(
//			q.criteria("controller").equal(controller),
//			q.criteria("method").equal(method),
//			q.criteria("status").equal("ERROR")
//		);
//		return q.asList();
		return null;
	}

}
