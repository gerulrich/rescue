package net.cloudengine.new_.cti;

import net.cloudengine.new_.cti.model.Call;
import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.QEntry;
import net.cloudengine.new_.cti.model.QMember;
import net.cloudengine.new_.cti.model.Queue;

/**
 * Listener para eventos provenientes de la central telefonica.
 * @author German Ulrich
 *
 */
public interface EventListener {
	
	
	// ~~ Eventos de conexión y desconexión ~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Evento de conexión establecida.
	 */
	void onConnect();
	
	/**
	 * Evento de conexión interrumpida.
	 */	
	void onDisconnect();
	
	
	// ~~ Eventos de estado de las extensiones ~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Notifica el cambio de estado de una extension.
	 * @param phoneNumber numero de extension (nro de interno)
	 * @param newStatus nuevo estado de la extensión.
	 */
	void extensionChanged(Extension extension);
	
	/**
	 * Notifica el estado de una nuva extensión.
	 */	
	void extensionAdded(Extension extension);
	
	
	// ~~ Eventos de estado de las colas ~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Notifica la existencia de una cola.
	 * Se notifica una vez por cada cola.
	 * @param queue
	 */
	void queueAdded(Queue queue);
	
	/**
	 * Notifica que un miembro se unió a la cola
	 * @param queue
	 * @param member
	 */
	void queueMemberAdded(String queue, QMember member);
	
	/**
	 * Notifica que un miembro se retiró a la cola
	 * @param queue
	 * @param member
	 */
	void queueMemberRemoved(String queue, QMember member);
	
	/**
	 * Notifica que se agregó una entrada en la cola.
	 * @param queue
	 * @param entry
	 */
	void queueEntryAdded(String queue, QEntry entry);
	
	/**
	 * Notifica que se retiró una entrada de la cola.
	 * @param queue
	 * @param entry
	 */
	void queueEntryRemoved(String queue, QEntry entry);
	
	// ~~ Eventos de estado de las llamadas ~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Notifica el establecimiento de una nueva llamada.
	 * @param call
	 */
	public void newCall(Call call);
	
	
	public void changeCall(Call call);
	
	/**
	 * Notifica la finalización de una llamada.
	 * @param call
	 */	
	public void hangupCall(Call call);

}
