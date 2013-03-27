package net.cloudengine.mapviewer.tools.selection;

import java.util.List;

import net.cloudengine.mapviewer.layers.Item;

public class Group {
	
	private String name;
	private List<Item> items;
	
	public Group(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public boolean isEmpty() {
		return items.isEmpty();
	}
	

}
