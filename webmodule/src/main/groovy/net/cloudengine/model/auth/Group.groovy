package net.cloudengine.model.auth

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.apache.bval.constraints.NotEmpty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="group")
class Group {
	
	@Id ObjectId id;
	
	@NotNull
	@NotEmpty
	@Size(min=1,max=16)
	String name;
	
	@Size(min=0,max=32)
	String description;

}
