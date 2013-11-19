package net.cloudengine.rpc.mappers;

import net.cloudengine.rpc.mappers.transformers.DateToStringTransformer;

@DataObject
public class ParentChildFlatModel {
	
	private Boolean enabled;
	@Value(value="child.childName")
	private String name;
	@Value(value="child.childNumber")
	private Integer number;
	
	@Value(value = "child.date", transformer=DateToStringTransformer.class)
	private String date;
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
