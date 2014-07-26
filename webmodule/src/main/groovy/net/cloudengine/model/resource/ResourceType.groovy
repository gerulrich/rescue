package net.cloudengine.model.resource

import groovy.transform.ToString;

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.validation.constraints.NotNull;

import net.cloudengine.model.commons.Identifiable;
import net.cloudengine.web.crud.support.CrudProperty;

import org.apache.bval.constraints.NotEmpty;

@Entity
@ToString
class ResourceType implements Identifiable<Long> {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;
	
	@NotNull
	@NotEmpty
	@CrudProperty
	String name;
	@Lob byte[] image;
	@Override
	
	public Long getPK() {
		return id;
	}

}
