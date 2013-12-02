package net.cloudengine.model.config

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="property")
class AppProperty {
	
	@Id ObjectId id;
	
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
