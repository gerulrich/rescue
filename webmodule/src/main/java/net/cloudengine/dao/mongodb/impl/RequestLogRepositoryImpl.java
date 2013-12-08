package net.cloudengine.dao.mongodb.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import net.cloudengine.dao.mongodb.MongoRepository;
import net.cloudengine.dao.mongodb.RequestLogRepository;
import net.cloudengine.model.statistics.RequestLog;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RequestLogRepositoryImpl extends MongoRepository<RequestLog, ObjectId> implements RequestLogRepository {

	public RequestLogRepositoryImpl(MongoTemplate mongoTemplate) {
		super(RequestLog.class, mongoTemplate);
	}

	@Override
	public List<RequestLog> getByControllerAndMethod(String controller, String method) {
		return mongoTemplate.find(query(
				where("controller").is(controller).andOperator(
				where("controller").is(controller))), getType());
	}

}
