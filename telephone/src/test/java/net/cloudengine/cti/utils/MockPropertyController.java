package net.cloudengine.cti.utils;

import java.util.Collection;

import net.cloudengine.rpc.controller.config.PropertyController;
import net.cloudengine.rpc.controller.config.PropertyModel;

public class MockPropertyController implements PropertyController {

	
	@Override
	public Collection<PropertyModel> getProperties() {
		throw new RuntimeException("No implementado");
	}

	@Override
	public PropertyModel getProperty(String key) {
		throw new RuntimeException("No implementado");
	}

}
