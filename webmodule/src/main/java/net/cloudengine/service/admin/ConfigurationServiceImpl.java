package net.cloudengine.service.admin;

import java.util.ArrayList;
import java.util.Collection;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;
import net.cloudengine.model.config.AppProperty;

import org.springframework.data.document.mongodb.MongoDbFactory;

public class ConfigurationServiceImpl implements ConfigurationService {

	private Datastore<AppProperty, Long> datastore;
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
	
	public void setDatastore(Datastore<AppProperty, Long> datastore) {
		this.datastore = datastore;
	}

	public Collection<ConfigOption> getAvailableServices() {
		Collection<ConfigOption> options = new ArrayList<ConfigOption>();

		if (mongoDbFactory != null) {
			options.add(new ConfigOption("MongoDB", mongoDbFactory.getDb().getMongo().getAddress().toString()));
		}
		options.add(new ConfigOption("Version", version));
		options.add(new ConfigOption("Build", buildNumber));
		
		String executionType = (System.getenv("VCAP_APPLICATION") != null) ? "Cloud" : "Local";
		options.add(new ConfigOption("Ejecucion", executionType));
		
		return options;
	}

	@Override
	public AppProperty getProperty(String key) {
		return datastore.createQuery().field("key").eq(key).get();
	}

	@Override
	public Collection<AppProperty> getAll() {
		PagingResult<AppProperty> pageResult = datastore.list();
		return pageResult.getList();
	}

	@Override
	public Collection<AppProperty> getAllClientProperties() {
		Query<AppProperty> q = datastore.createQuery();
		return q.field("clientProperty").eq(Boolean.TRUE).list();
	}
	
	

}
