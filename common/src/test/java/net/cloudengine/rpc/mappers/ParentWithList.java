package net.cloudengine.rpc.mappers;

import java.util.ArrayList;
import java.util.List;

public class ParentWithList {
	
	private List<ListElement> elements = new ArrayList<>();

	public List<ListElement> getElements() {
		return elements;
	}

	public void setElements(List<ListElement> elements) {
		this.elements = elements;
	}
}
