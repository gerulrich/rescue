package net.cloudengine.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.document.mongodb.SimpleMongoDbFactory;

public class MongoDBWrapper {
	
	private SimpleMongoDbFactory factory;
	
	@Autowired
	public MongoDBWrapper(@Qualifier("mongoDbFactory") SimpleMongoDbFactory factory) {
		this.factory = factory;
	}

	public SimpleMongoDbFactory getFactory() {
		return factory;
	}			

}
