package net.cloudengine.service.admin;

import java.util.Collection;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;
import net.cloudengine.model.config.AppProperty;

public class ConfigurationServiceImpl implements ConfigurationService {

	private Datastore<AppProperty, Long> datastore;
	private String version;
	private String buildNumber;
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public void setDatastore(Datastore<AppProperty, Long> datastore) {
		this.datastore = datastore;
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

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getBuildNumber() {
		return buildNumber;
	}
	
	

}
