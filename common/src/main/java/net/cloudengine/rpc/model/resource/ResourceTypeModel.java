package net.cloudengine.rpc.model.resource;

import java.io.Serializable;
import java.util.Arrays;

import net.cloudengine.rpc.model.DataObject;

@DataObject
public class ResourceTypeModel implements Serializable {

	private static final long serialVersionUID = 505955027356275866L;
	
	private Long id;
	private String name;
	private byte image[];
	
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
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = Arrays.copyOf(image, image.length);
	}
	
	

}
