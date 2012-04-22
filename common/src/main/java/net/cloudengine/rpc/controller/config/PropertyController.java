package net.cloudengine.rpc.controller.config;

import java.util.Collection;

public interface PropertyController {
	
	Collection<PropertyModel> getProperties();
	
	PropertyModel getProperty(String key);

}
