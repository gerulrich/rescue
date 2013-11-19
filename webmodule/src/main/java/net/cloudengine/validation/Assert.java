package net.cloudengine.validation;

/**
 * Clase utilitaria que ayuda a la validaci&oacute;n de argumentos. Es &uacute;til para
 * identificar errores de programaci&oacute;n en forma temprana y clara.
 * 
 * Normalmente se utiliza para validar los argumentos de los m&eacute;todos para
 * comprobar casos que usualmente son errores de programaci&oacute;n. 
 * 
 * @author German Ulrich
 *
 */
public final class Assert {
	
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notEmpty(String object, String message) {
		if (object == null || object.trim().equals("")) {
			throw new IllegalArgumentException(message);
		}
	}	

}
