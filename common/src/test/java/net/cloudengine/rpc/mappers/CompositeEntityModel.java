package net.cloudengine.rpc.mappers;

@DataObject
public class CompositeEntityModel {
	
	@Id @Value(value="id") private Long oid;
	private String name;
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
