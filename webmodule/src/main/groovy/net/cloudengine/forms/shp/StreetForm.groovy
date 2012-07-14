package net.cloudengine.forms.shp

import javax.validation.constraints.NotNull;

class StreetForm extends POIForm {
	
	@NotNull
	String fromLeftField;
	@NotNull
	String toLeftField;
	
	@NotNull
	String fromRightField;
	
	@NotNull
	String toRightField;
	
	@NotNull
	String vstartField;
	
	@NotNull
	String vendField;

}
