package net.cloudengine.rpc.service.config;

import java.util.Collection;

import net.cloudengine.mappers.DTOMapper;
import net.cloudengine.mappers.MappersRegistry;
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
	private MappersRegistry mappersRegistry;
	
	@Autowired
	public PropertyControllerImpl(ConfigurationService service) {
		this.service = service;
	}
	
	@Autowired
	public void setMappersRegistry(MappersRegistry mappersRegistry) {
		this.mappersRegistry = mappersRegistry;
	}	
	
	@Override
	public Collection<PropertyModel> getProperties() {
		Collection<AppProperty> props = service.getAllClientProperties();
		DTOMapper propertyMapper = mappersRegistry.getMapper(PropertyModel.class);
		return propertyMapper.fillModels(props, PropertyModel.class);
	}

	@Override
	public PropertyModel getProperty(String key) {
		AppProperty property = service.getProperty(key);
		if (property != null && property.isClientProperty()) {
			DTOMapper propertyMapper = mappersRegistry.getMapper(PropertyModel.class);
			return propertyMapper.fillModel(property, PropertyModel.class);
		} else {
			return null;
		}
	}
}
