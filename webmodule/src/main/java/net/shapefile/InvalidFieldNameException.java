package net.shapefile;

public class InvalidFieldNameException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidFieldNameException() {
	}

	public InvalidFieldNameException(String paramString) {
		super(paramString);
	}
}