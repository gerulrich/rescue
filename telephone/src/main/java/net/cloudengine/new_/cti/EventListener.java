package net.cloudengine.new_.cti;

import net.cloudengine.pbx.Status;

/**
 * Listener para eventos provenientes de la central telefonica.
 * @author German Ulrich
 *
 */
public interface EventListener {
	
	/**
	 * Evento de conexión establecida.
	 */
	void onConnect();
	
	/**
	 * Evento de conexión interrumpida.
	 */	
	void onDisconnect();
	
	/**
	 * Notifica el cambio de estado de una extension.
	 * @param phoneNumber numero de extension (nro de interno)
	 * @param newStatus nuevo estado de la extensión.
	 */
	void extensionChanged(String phoneNumber, Status newStatus);

}
