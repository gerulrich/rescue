package net.cloudengine.rpc.controller.ticket;

import java.util.List;

import net.cloudengine.rest.model.resource.Response;
import net.cloudengine.rest.model.ticket.TicketModel;
import net.cloudengine.rpc.model.ActionModel;

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
	 * Actualiza la informaci&oacute;n de un ticket.
	 * @param ticket
	 */
	boolean update(TicketModel ticketModel);
	
	/**
	 * Asigna el ticket al usuario actual.
	 * @param ticketId
	 * @param force
	 */
	boolean assignToMe(Long ticketId, boolean force);
	
	void leaveTicket(Long ticketId);
	
	
	Response<String> exec(Long ticketId, String action);
	Response<String> fork(Long ticketId, String action, List<String> groupIds);
	
	List<ActionModel> getActions(Long ticketId);

}
