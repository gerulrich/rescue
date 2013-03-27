package net.cloudengine.rpc.controller.geo;

import java.io.Serializable;

import net.cloudengine.rpc.model.DataObject;

@DataObject
public class ZoneModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String type;
	private String geometry;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGeometry() {
		return geometry;
	}
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	
}
