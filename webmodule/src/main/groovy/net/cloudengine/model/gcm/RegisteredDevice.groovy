package net.cloudengine.model.gcm

import net.cloudengine.validation.Email

import org.apache.bval.constraints.NotEmpty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="registered_device")
class RegisteredDevice {
	
	@Id ObjectId id;
	
	@NotEmpty
	@Email
	String username
	
	Set<String> devices;

}
