package net.dbf;

public class RecordField {
	
	private String name;
	private String value;

	public RecordField() {
	}

	public RecordField(String paramString) {
		this.name = paramString;
	}

	public RecordField(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}