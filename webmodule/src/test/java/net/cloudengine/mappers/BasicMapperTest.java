package net.cloudengine.mappers;

import junit.framework.TestCase;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.config.AppProperty;
import net.cloudengine.rpc.controller.auth.UserModel;
import net.cloudengine.rpc.controller.config.PropertyModel;

import org.junit.Test;

public class BasicMapperTest extends TestCase {

	@Test
	public void testMapperUser() {
		User user = new User();
		user.setUsername("admin@admin.com");
		user.setDisplayName("Administrador");
		
		BasicMapper mapper = new BasicMapper();
		UserModel userModel = mapper.fillModel(user, UserModel.class);
		
		assertNotNull(userModel);
		assertEquals(user.getUsername(), userModel.getUsername());
		assertEquals(user.getDisplayName(), userModel.getDisplayName());
		
	}
	
	@Test
	public void testAppProperty() {
		AppProperty property = new AppProperty("prueba.value", "valor");
		
		
		BasicMapper mapper = new BasicMapper();
		PropertyModel propModel = mapper.fillModel(property, PropertyModel.class);
		
		assertNotNull(propModel);
		assertEquals(property.getKey(), propModel.getName());
		assertEquals(property.getValue(), propModel.getValue());
		
	}

}
