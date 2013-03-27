package net.cloudengine.web.crud.role;

import net.cloudengine.model.auth.Permission;

public class ExtendedPermission extends Permission {
	
	private static final long serialVersionUID = 1L;
	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
		
	
}