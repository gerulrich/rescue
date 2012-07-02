package net.shapefile;

public class InvalidFieldTypeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidFieldTypeException() {
	}

	public InvalidFieldTypeException(String paramString) {
		super(paramString);
	}
}