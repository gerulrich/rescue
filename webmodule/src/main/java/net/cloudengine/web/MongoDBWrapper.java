package net.cloudengine.web;

import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

public class MongoDBWrapper {
	
	private SimpleMongoDbFactory factory;
	
//	@Autowired
	public MongoDBWrapper(/*@Qualifier("mongoDbFactory")*/ SimpleMongoDbFactory factory) {
		this.factory = factory;
	}

	public SimpleMongoDbFactory getFactory() {
		return factory;
	}			

}
