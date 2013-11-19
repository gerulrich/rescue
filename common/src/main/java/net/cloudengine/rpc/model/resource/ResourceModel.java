package net.cloudengine.rpc.model.resource;

import java.io.Serializable;

import net.cloudengine.rpc.mappers.DataObject;
import net.cloudengine.rpc.mappers.Value;

@DataObject
public class ResourceModel implements Serializable {

	private static final long serialVersionUID = 505955027356275866L;
	
	private long id;
	private String name;
	@Value(value="type.id")
	private Long type;
	private double lon;
	private double lat;
	private long version;
	@Value(value="type.name")
	private String typeName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
