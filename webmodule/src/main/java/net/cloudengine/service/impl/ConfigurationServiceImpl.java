package net.cloudengine.service.impl;

import static net.cloudengine.dao.support.SearchParametersBuilder.forClass;

import java.util.Collection;

import net.cloudengine.dao.support.Page;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.SearchParametersBuilder;
import net.cloudengine.model.config.AppProperty;
import net.cloudengine.service.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {

	private Repository<AppProperty, Long> repository;
	private String version;
	private String buildNumber;
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public void setDatastore(Repository<AppProperty, Long> datastore) {
		this.repository = datastore;
	}

	@Override
	public AppProperty getProperty(String key) {
		SearchParametersBuilder builder = forClass(AppProperty.class);
		builder.eq("key", key);
		return repository.findOne(builder.build());
	}

	@Override
	public Collection<AppProperty> getAll() {
		Page<AppProperty> pageResult = repository.list();
		return pageResult.getList();
	}

	@Override
	public Collection<AppProperty> getAllClientProperties() {
		SearchParametersBuilder builder = forClass(AppProperty.class);
		builder.eq("clientProperty", Boolean.TRUE);
		return repository.findAll(builder.build());
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
