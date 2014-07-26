package net.cloudengine.rpc.model;

import java.io.Serializable;

import net.cloudengine.rpc.mappers.DataObject;

@DataObject
public class ActionModel implements Serializable {

	private static final long serialVersionUID = -462307659516470568L;
	
	private String name;
	private String description;
	boolean fork;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isFork() {
		return fork;
	}
	public void setFork(boolean fork) {
		this.fork = fork;
	}
}
