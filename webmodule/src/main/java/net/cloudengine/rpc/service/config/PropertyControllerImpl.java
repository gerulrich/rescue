package net.cloudengine.rpc.service.config;

import java.util.ArrayList;
import java.util.Collection;

import net.cloudengine.model.config.AppProperty;
import net.cloudengine.rpc.controller.config.PropertyController;
import net.cloudengine.rpc.controller.config.PropertyModel;
import net.cloudengine.service.admin.ConfigurationService;
import net.cloudengine.util.ExternalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ExternalService(exportedInterface=PropertyController.class)
public class PropertyControllerImpl implements PropertyController {

	private ConfigurationService service;
	
	@Autowired
	public PropertyControllerImpl(ConfigurationService service) {
		this.service = service;
	}
	
	@Override
	public Collection<PropertyModel> getProperties() {
		Collection<AppProperty> props = service.getAllClientProperties();
		Collection<PropertyModel> result = new ArrayList<PropertyModel>();
		for(AppProperty prop : props) {
			result.add(new PropertyModel(prop.getKey(), prop.getValue()));
		}
		return result;
	}

	@Override
	public PropertyModel getProperty(String key) {
		AppProperty property = service.getProperty(key);
		if (property != null && property.isClientProperty()) {
			PropertyModel model = new PropertyModel(key, property.getValue());
			return model;
		} else {
			return null;
		}
	}

	

}
