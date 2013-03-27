package net.cloudengine.model.auth

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.apache.bval.constraints.NotEmpty
import org.bson.types.ObjectId

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id

@Entity(value="group", noClassnameStored=true)
class Group {
	
	@Id ObjectId id;
	
	@NotNull
	@NotEmpty
	@Size(min=1,max=16)
	String name;
	
	@Size(min=0,max=32)
	String description;

}
