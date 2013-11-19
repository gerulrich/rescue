package net.cloudengine.rpc.mappers;

import java.util.List;

@DataObject
public class ParentWithListModel {
	
	@ListValue
	private List<ListElementModel> elements;

	public List<ListElementModel> getElements() {
		return elements;
	}

	public void setElements(List<ListElementModel> elements) {
		this.elements = elements;
	}

}
