package net.cloudengine.service.admin;

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

	public Collection<ConfigOption> getAvailableServices() {
		Collection<ConfigOption> options = new ArrayList<ConfigOption>();

		if (mongoDbFactory != null) {
			options.add(new ConfigOption("MongoDB", mongoDbFactory.getDb().getMongo().getAddress().toString()));
		}
		options.add(new ConfigOption("Version", version));
		options.add(new ConfigOption("Build", buildNumber));
		
		String executionType = (System.getenv("VCAP_APPLICATION") != null) ? "En la nube" : "Local";
		options.add(new ConfigOption("Ejecucion", executionType));
		
		return options;
	}

}
