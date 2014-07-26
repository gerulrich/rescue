package net.cloudengine.rpc.controller.geo;

import java.io.Serializable;

import net.cloudengine.rpc.mappers.DataObject;

@DataObject
public class StreetBlockModel implements Serializable {
	
	private Long id;
	private String name;
	private int vstart;
	private int vend;
	
	private double x;
	private double y;
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
	public int getVstart() {
		return vstart;
	}
	public void setVstart(int vstart) {
		this.vstart = vstart;
	}
	public int getVend() {
		return vend;
	}
	public void setVend(int vend) {
		this.vend = vend;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

}
