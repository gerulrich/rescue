package net.cloudengine.rpc.mappers;


@DataObject
public class SimpleObjectModel {
	
	private Long id;
	private String name;
	private String value;
	
	@Value(value="property")
	private String customProperty;
	
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCustomProperty() {
		return customProperty;
	}
	public void setCustomProperty(String customProperty) {
		this.customProperty = customProperty;
	}
}
