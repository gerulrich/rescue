package net.cloudengine.forms.shp

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId

class FileSelectForm {
	
	@NotNull
	String file;
	
	@NotNull
	String type;

}
