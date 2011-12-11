package net.cloudengine.web.admin;

public class MongoCollection {
	private String name;
	private long size;

	public MongoCollection(String name, long size) {
		super();
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}
}