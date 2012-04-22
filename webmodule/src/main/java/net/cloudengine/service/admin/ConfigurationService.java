package net.cloudengine.service.admin;

import java.util.Collection;

import net.cloudengine.model.config.AppProperty;

public interface ConfigurationService {
	
	Collection<ConfigOption> getAvailableServices();
	AppProperty getProperty(String key);
	Collection<AppProperty> getAll();
	Collection<AppProperty> getAllClientProperties();
	

}
