package net.cloudengine.rpc.mappers;

import net.cloudengine.rpc.mappers.transformers.DateToStringTransformer;

@DataObject
public class ChildObjectModel {

	private String childName;
	private Integer childNumber;
	@Value(value="date", transformer=DateToStringTransformer.class)
	private String date;

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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
