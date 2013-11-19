package net.cloudengine.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Contiene funciones utilitarias para calcular resúmenes criptográficos.
 * 
 * @author German Ulrich
 */
public class Cipher {

	private static final String ALGORITHM = "SHA";

	/**
	 * Calcula el resumen criptográfico de un string con el algoritmo SHA.
	 * 
	 * @param clearText texto a cifrar
	 * @return texto cifrado con SHA.
	 */
	public String encrypt(String plainText) {
		return encrypt(plainText, ALGORITHM);
	}
	
	/**
	 * Calcula el resumen criptográfico de un string con el algoritmo especificado.
	 * 
	 * @param clearText texto a cifrar
	 * @return texto cifrado.
	 * @throws IllegalArgumentException si el algoritmo es inválido.
	 */
	public String encrypt(String plainText, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(plainText.getBytes());
			return HexString.bufferToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
}