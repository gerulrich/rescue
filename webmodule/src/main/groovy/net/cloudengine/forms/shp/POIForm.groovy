package net.cloudengine.forms.shp

import javax.validation.constraints.NotNull;

class POIForm {

	@NotNull
	String nameField;
	
	@NotNull
	String typeField;
	
	boolean overwrite;
	
	
}
