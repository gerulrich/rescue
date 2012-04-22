package net.cloudengine.web;

import org.springframework.data.document.mongodb.SimpleMongoDbFactory;

public class MongoDBWrapper {
	
	private SimpleMongoDbFactory factory;
	
	public MongoDBWrapper(SimpleMongoDbFactory factory) {
		this.factory = factory;
	}

	public SimpleMongoDbFactory getFactory() {
		return factory;
	}			

}
