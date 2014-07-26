package net.cloudengine.service;

import java.util.List;

import net.cloudengine.domain.workflow.Action;
import net.cloudengine.domain.workflow.OperationResult;
import net.cloudengine.model.ticket.Ticket;
import net.cloudengine.rest.model.ticket.TicketModel;
import net.cloudengine.rpc.mappers.DTOMapper;

public interface TicketService {
	
	Ticket createTicket();
	
	Ticket get(Long id);
	
	boolean update(TicketModel ticketModel, DTOMapper mapper);
	
	OperationResult exec(Long ticketId, String action);
	OperationResult fork(Long ticketId, String action, List<String> groupIds);
	
	boolean assignToMe(Long ticketId, boolean force);
	
	void leaveTicket(Long ticketId);
	
	List<Action> getActions(Long ticketId);

}
