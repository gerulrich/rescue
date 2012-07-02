package net.shapefile;

public class InvalidFileException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidFileException() {
	}

	public InvalidFileException(String paramString) {
		super(paramString);
	}
}