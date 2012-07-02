package net.dbf;

public class TableDescriptor {
	private String name;
	private int type = 101;
	private int width;
	private int precision = 0;

	public TableDescriptor() {
	}

	public TableDescriptor(String name) {
		this.name = name;
	}

	public TableDescriptor(String name, int type) {
		this.name = name;
		this.type = type;
	}

	public TableDescriptor(String name, int type, int width) {
		this.name = name;
		this.type = type;
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}
}