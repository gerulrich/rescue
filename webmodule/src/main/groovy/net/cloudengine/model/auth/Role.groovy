package net.cloudengine.model.auth

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

import org.bson.types.ObjectId

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id
import com.google.code.morphia.annotations.Reference

@Entity(value="role", noClassnameStored=true)
class Role {

	@Id ObjectId id;
	
	@NotNull
	@Pattern(regexp=".+")
	String name;
	
	@NotNull
	@Pattern(regexp=".+")
	String description;
	
	@Reference
	List<Permission> permissions;
	
}
