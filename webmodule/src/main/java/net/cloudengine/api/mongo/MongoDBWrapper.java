package net.cloudengine.api.mongo;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

public class MongoDBWrapper implements InitializingBean {

	private SimpleMongoDbFactory factory;
	private String username = null;
	private String password = null;
	
	public MongoDBWrapper(SimpleMongoDbFactory factory) {
		this.factory = factory;
	}

	public SimpleMongoDbFactory getFactory() {
		return factory;
	}
	
	public String getURI() {
		return "mongodb://"+
				factory.getDb().getMongo().getAddress().getHost()+
				":"+
				factory.getDb().getMongo().getAddress().getPort()+
				"/"+
				factory.getDb().getName();
	}
	
	public String getUser() {
		return username;
	}
	
	public String getPass() {
		return password;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.parseMongoConfiguration(System.getenv("VCAP_SERVICES"));
	}
	
	public void parseMongoConfiguration(String conf) {
		if (StringUtils.isEmpty(conf)) {
			return;
		}
		try {
			
			ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(conf);
            Iterator<JsonNode> it = rootNode.getElements();
            while(it.hasNext()) {
            	JsonNode childElement = it.next();
            	JsonNode configNode = childElement.get(0);
            	String name = configNode.get("name").getTextValue();
            	if ("MyMongoDB".equals(name)) {
            		JsonNode credencialsNode = configNode.get("credentials");
            		username = credencialsNode.get("username").getTextValue();
            		password = credencialsNode.get("password").getTextValue();
            	}
            }            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
