package net.cloudengine.rpc.mappers;

import java.util.Date;

public class ChildObject {

	private String childName;
	private Integer childNumber;
	private Date date;
	
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public Integer getChildNumber() {
		return childNumber;
	}
	public void setChildNumber(Integer childNumber) {
		this.childNumber = childNumber;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
