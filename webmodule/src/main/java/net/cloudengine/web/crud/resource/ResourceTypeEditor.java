package net.cloudengine.web.crud.resource;

import java.beans.PropertyEditorSupport;

import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.service.admin.ResourceService;

public class ResourceTypeEditor extends PropertyEditorSupport {

	private ResourceService service;
	
	public ResourceTypeEditor(ResourceService service) {
		super();
		this.service = service;
	}

	@Override
	public void setAsText(String text) {
		setValue(service.getType(Long.valueOf(text)));
	}

	@Override
	public String getAsText() {
		if (getValue() != null) {
			return ((ResourceType)getValue()).getId().toString();
		} return "";
	}
}