package net.cloudengine.rpc.mappers;

@DataObject
public class BasicEntityModel {
	
	private Long id;
	private String name;
	private CompositeEntityModel composite;
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
	public CompositeEntityModel getComposite() {
		return composite;
	}
	public void setComposite(CompositeEntityModel composite) {
		this.composite = composite;
	}	
}
