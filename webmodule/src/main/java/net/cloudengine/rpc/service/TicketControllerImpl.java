package net.cloudengine.rpc.service;

import java.util.List;

import net.cloudengine.domain.workflow.OperationResult;
import net.cloudengine.management.ExternalService;
import net.cloudengine.model.ticket.Ticket;
import net.cloudengine.rest.model.resource.Response;
import net.cloudengine.rest.model.ticket.TicketModel;
import net.cloudengine.rpc.controller.ticket.TicketController;
import net.cloudengine.rpc.mappers.DTOMapper;
import net.cloudengine.rpc.mappers.MappersRegistry;
import net.cloudengine.rpc.model.ActionModel;
import net.cloudengine.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ExternalService(exportedInterface=TicketController.class)
@Transactional
public class TicketControllerImpl implements TicketController {

	private TicketService ticketService;
	private MappersRegistry mappersRegistry;
	
	@Autowired
	public TicketControllerImpl(TicketService ticketService, MappersRegistry mappersRegistry) {
		super();
		this.ticketService = ticketService;
		this.mappersRegistry = mappersRegistry;
	}

	@Override
	public TicketModel create() {
		DTOMapper mapper = mappersRegistry.getMapper(TicketModel.class);
		Ticket ticket = ticketService.createTicket();
		return mapper.fillModel(ticket, TicketModel.class);
	}
	
	@Override
	public TicketModel get(Long id) {
		DTOMapper mapper = mappersRegistry.getMapper(TicketModel.class);
		Ticket ticket = ticketService.get(id);
		return mapper.fillModel(ticket, TicketModel.class);
	}

	@Override
	public boolean update(TicketModel ticketModel) {
		DTOMapper mapper = mappersRegistry.getMapper(TicketModel.class);
		return ticketService.update(ticketModel, mapper);
	}
	
	public void joinGroup(Long ticketId, String groupId) {
	}
	
	@Override
	public boolean assignToMe(Long ticketId, boolean force) {
		return ticketService.assignToMe(ticketId, force);
	}
	
	@Override
	public void leaveTicket(Long ticketId) {
		ticketService.leaveTicket(ticketId);
	}
	
	@Override
	public Response<String> exec(Long ticketId, String action) {
		OperationResult operationResult = ticketService.exec(ticketId, action);
		if (operationResult.getCode() == OperationResult.OK) {
			return new Response<String>(Response.OK_RESPONSE, null, Response.SUCCESSFUL);
		} else {
			return new Response<String>(Response.ERROR_RESPONSE, null, operationResult.getMessage());
		}
	}

	@Override
	public Response<String> fork(Long ticketId, String action, List<String> groupIds) {
		OperationResult operationResult = ticketService.fork(ticketId, action, groupIds);
		if (operationResult.getCode() == OperationResult.OK) {
			return new Response<String>(Response.OK_RESPONSE, null, Response.SUCCESSFUL);
		} else {
			return new Response<String>(Response.ERROR_RESPONSE, null, operationResult.getMessage());
		}
	}

	@Override
	public List<ActionModel> getActions(Long ticketId) {
		DTOMapper mapper = mappersRegistry.getMapper(ActionModel.class);
		return mapper.fillModels(ticketService.getActions(ticketId), ActionModel.class);
	}
	
}
