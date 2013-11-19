package net.cloudengine.rpc.controller.ticket;

import net.cloudengine.rest.model.ticket.TicketModel;

public interface TicketController {
	
	/**
	 * Obtiene un ticket a partir de su id.
	 * @param id
	 * @return
	 */
	TicketModel get(Long id);
	
	/**
	 * Crea un nuevo ticket.
	 * @return
	 */
	TicketModel create();
	
	/**
	 * Actualiza el estado de un ticket.
	 * @param ticket
	 */
	boolean update(TicketModel ticketModel);
	
	/**
	 * Une un grupo al ticket.
	 * @param ticketId
	 * @param groupId
	 */
	void joinGroup(Long ticketId, String groupId);
	
	/**
	 * Asigna el ticket al usuario actual.
	 * @param ticketId
	 * @param force
	 */
	boolean assignToMe(Long ticketId, boolean force);
	
	void leaveTicket(Long ticketId);
	

}
