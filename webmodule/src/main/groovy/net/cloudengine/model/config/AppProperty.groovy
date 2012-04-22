package net.cloudengine.model.config

import org.bson.types.ObjectId

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id


@Entity(value="property", noClassnameStored=true)
class AppProperty {
	
	@Id
	ObjectId id;
	
	String key;
	
	String value; 
	
	boolean clientProperty;
	
	public AppProperty() {
		super();
	}
	
	public AppProperty(String key, String value) {
		this(key, value, false);
	}
	
	public AppProperty(String key, String value, boolean clientProperty) {
		this.key = key;
		this.value = value;
		this.clientProperty = clientProperty;
	}

}
