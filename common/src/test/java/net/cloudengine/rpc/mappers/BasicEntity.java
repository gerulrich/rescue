package net.cloudengine.rpc.mappers;

public class BasicEntity {
	
	private Long id;
	private String name;
	private CompositeEntity composite;
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
	public CompositeEntity getComposite() {
		return composite;
	}
	public void setComposite(CompositeEntity composite) {
		this.composite = composite;
	}
}
