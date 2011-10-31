package net.cloudengine.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.document.mongodb.MongoDbFactory;

public class ConfigurationServiceImpl implements ConfigurationService {

	private MongoDbFactory mongoDbFactory;
	private String version;
	private String buildNumber;
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setMongoDbFactory(MongoDbFactory mongoDbFactory) {
		this.mongoDbFactory = mongoDbFactory;
	}
	
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public Collection<String> getAvailableServices() {
		Collection<String> services = new ArrayList<String>();

		if (mongoDbFactory != null) {
			services.add("MongoDB: "
					+ mongoDbFactory.getDb().getMongo().getAddress());
		}
		services.add("Version: " +version);
		services.add("Build: " +buildNumber);
		
		String environmentName = (System.getenv("VCAP_APPLICATION") != null) ? "Ejecutando en la Nube" : "Ejecutando Localmente";
		services.add(environmentName);
		return services;
	}

}
