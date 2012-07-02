package net.shapefile;

public class InvalidShapeTypeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidShapeTypeException() {
	}

	public InvalidShapeTypeException(String paramString) {
		super(paramString);
	}
}