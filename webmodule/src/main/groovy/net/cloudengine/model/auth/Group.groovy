package net.cloudengine.model.auth

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import net.cloudengine.model.commons.Identifiable;
import net.cloudengine.web.crud.support.CrudProperty;

import org.apache.bval.constraints.NotEmpty
import org.bson.types.ObjectId
import org.simple.workflow.entity.DistributionGroup;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="group")
class Group implements Identifiable<ObjectId>, DistributionGroup, Serializable {
	
	@Id ObjectId id;
	
	@NotNull
	@NotEmpty
	@Size(min=1,max=16)
	@CrudProperty
	String name;
	
	@Size(min=0,max=32)
	@CrudProperty
	String description;

	@Override
	public ObjectId getPK() {
		return id;
	}

	@Override
	public String getOID() {
		return id.toString();
	}
}
