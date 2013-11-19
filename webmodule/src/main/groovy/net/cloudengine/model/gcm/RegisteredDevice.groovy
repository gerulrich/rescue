package net.cloudengine.model.gcm

import net.cloudengine.validation.Email

import org.apache.bval.constraints.NotEmpty
import org.bson.types.ObjectId

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id

@Entity(value="registered_device", noClassnameStored=true)
class RegisteredDevice {
	
	@Id
	ObjectId id;
	
	@NotEmpty
	@Email
	String username
	
	Set<String> devices;

}
