package net.cloudengine.model.auth

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="role")
class Role {

	@Id ObjectId id;
	
	@NotNull
	@Pattern(regexp=".+")
	String name;
	
	@NotNull
	@Pattern(regexp=".+")
	String description;
	
	@DBRef
	List<Permission> permissions;
	
}
