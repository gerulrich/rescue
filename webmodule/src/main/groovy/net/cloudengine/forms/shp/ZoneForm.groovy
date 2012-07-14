package net.cloudengine.forms.shp

import javax.validation.constraints.NotNull;

class ZoneForm {
	
	@NotNull
	String nameField;
	
	@NotNull
	String type;
	
	boolean overwrite;

}
