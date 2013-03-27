package net.cloudengine.client.xmap;

import net.cloudengine.mapviewer.layers.Item;
import net.cloudengine.mapviewer.tools.selection.Attribute;

public class ResourceItem implements Item {
	
	private Long id;
	@Attribute(label="Nombre")
	private String name;
	private double lon;
	private double lat;
	private long type;
	private boolean selected;
	@Attribute(label="Tipo")
	private String typeDescription;
	
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	@Override
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	
	@Override
	public boolean allowSelection() {
		return true;
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public void selectItem() {
		this.selected = true;
	}
	
	@Override
	public void unselectItem() {
		this.selected = false;
	}

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}	

}
