package net.cloudengine.util;

/**
 * Clase utilitaria que ayuda a la validación de argumentos. Es útil para
 * identificar errores de programación en forma temprana y clara.
 * 
 * Normalmente se utiliza para validar los argumentos de los métodos para
 * comprobar casos que usualmente son errores de programación. 
 * 
 * @author German Ulrich
 *
 */
public abstract class Assert {
	
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
