package net.cloudengine.util;

/**
 * Contiene funciones utilitarias para convertir array de bytes en String
 * en formato hexadecimal.
 * 
 */
public class HexString {
	
	/**
	 * Caracteres Hexadecimales.
	 */
	private static final char kHexChars[] = { 
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' 
	};	
	
	/**
	 * Obtiene un String conteniendo la representación hexadecimal del array de bytes
	 * que se pasa como argumento. Los caracteres de salida están en mayucusculas.
	 * 
	 * @param buffer el buffer a convertir a hexadecimal.
	 * @return la representación hexadecimal del byte array.
	 */
	public static String bufferToHex(byte buffer[]) {
		return HexString.bufferToHex(buffer, 0, buffer.length);
	}

	/**
	 * Obtiene un String conteniendo la representación hexadecimal del array de bytes
	 * que se pasa como argumento. Los caracteres de salida están en mayucusculas.
	 * 
	 * @param buffer el buffer a convertir a hexadecimal.
	 * @param startOffset el offset  del primer byte en el buffer a procesar
	 * @param length el numero de bytes a procesar
	 * @return la representación hexadecimal del byte array.
	 */
	public static String bufferToHex(byte buffer[], int startOffset, int length) {
		StringBuffer hexString = new StringBuffer(2 * length);
		int endOffset = startOffset + length;

		for (int i = startOffset; i < endOffset; i++) {
			HexString.appendHexPair(buffer[i], hexString);
		}

		return hexString.toString();
	}

	/**
	 * Appends a hexadecimal representation of a particular char value to a
	 * string buffer. That is, two hexadecimal digits are appended to the
	 * string.
	 * 
	 * @param b a byte whose hex representation is to be obtained
	 * @param hexString the string to append the hex digits to
	 */
	private static void appendHexPair(byte b, StringBuffer hexString) {
		char highNibble = kHexChars[(b & 0xF0) >> 4];
		char lowNibble = kHexChars[b & 0x0F];
		hexString.append(highNibble);
		hexString.append(lowNibble);
	}
}