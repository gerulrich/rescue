package net.cloudengine.service.admin;

import java.util.Collection;

import net.cloudengine.model.config.AppProperty;

public interface ConfigurationService {
	
	AppProperty getProperty(String key);
	Collection<AppProperty> getAll();
	Collection<AppProperty> getAllClientProperties();
	String getVersion();
	String getBuildNumber();
	
	

}
