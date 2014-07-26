package net.cloudengine.rpc.model;

import java.io.Serializable;
import java.util.List;

public class FolderTab<T> implements Serializable {

	private static final long serialVersionUID = 4921225771379272774L;

	private String name;
	private int page;
	private int size;
	private int total;
	private List<T> elements;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getElements() {
		return elements;
	}
	public void setElements(List<T> elements) {
		this.elements = elements;
	}	
	
}
