package net.cloudengine.web.crud.user;

import java.beans.PropertyEditorSupport;

import net.cloudengine.model.auth.Group;
import net.cloudengine.model.resource.ResourceType;

import org.bson.types.ObjectId;

public class GroupEditor extends PropertyEditorSupport {

	public GroupEditor() {
		super();
	}

	@Override
	public void setAsText(String text) {
		Group group = new Group();
		group.setId(new ObjectId(text));
		setValue(group);
	}

	@Override
	public String getAsText() {
		if (getValue() != null) {
			return ((ResourceType)getValue()).getId().toString();
		} return "";
	}
}