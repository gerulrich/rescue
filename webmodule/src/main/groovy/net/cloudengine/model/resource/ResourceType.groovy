package net.cloudengine.model.resource

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.validation.constraints.NotNull;

import org.apache.bval.constraints.NotEmpty;

@Entity
class ResourceType {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;
	
	@NotNull
	@NotEmpty
	String name;
	
	@Lob
	byte[] image;

	@Override
	public String toString() {
		return name.toString();
	}
	
		

}
