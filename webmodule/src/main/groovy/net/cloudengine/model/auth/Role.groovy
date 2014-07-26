package net.cloudengine.model.auth

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

import net.cloudengine.web.crud.support.CrudProperty;

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="role")
class Role {

	@Id ObjectId id;
	
	@NotNull
	@Pattern(regexp=".+")
	@CrudProperty
	String name;
	
	@NotNull
	@Pattern(regexp=".+")
	@CrudProperty
	String description;
	
	@DBRef
	List<Permission> permissions = new ArrayList<Permission>();
	
}
