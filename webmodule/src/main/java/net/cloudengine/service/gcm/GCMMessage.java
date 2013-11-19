package net.cloudengine.service.gcm;

/**
 * Representa un mensaje para ser enviado a
 * dispositivos android.
 * @author German Ulrich
 *
 */
public interface GCMMessage {
	
	/**
	 * Retorna el tipo de mensaje.
	 * @return
	 */
	String getType();
	
	/**
	 * Retorna el cuerpo del mensaje.
	 * @return
	 */
	String getBody();
	
}
