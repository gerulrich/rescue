package net.cloudengine.rest.model.resource;

import net.cloudengine.rpc.mappers.DataObject;
import net.cloudengine.rpc.mappers.Value;
import net.cloudengine.rpc.mappers.transformers.DateToStringTransformer;
import net.cloudengine.rpc.mappers.transformers.ObjectToString;

@DataObject
public class ReportModel {

	@Value(value="fileName")
	private String name;
	@Value(value="username")
	private String owner;
	@Value(value="date", transformer=DateToStringTransformer.class)
	private String date;
	@Value(value="id", transformer=ObjectToString.class)
	private String oid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

}
