package net.cloudengine.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.domain.workflow.Action;
import net.cloudengine.domain.workflow.OperationResult;
import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.ticket.Ticket;
import net.cloudengine.model.ticket.WorkBook;
import net.cloudengine.rest.model.ticket.TicketModel;
import net.cloudengine.rpc.mappers.DTOMapper;
import net.cloudengine.service.SessionService;
import net.cloudengine.service.TicketService;
import net.cloudengine.service.amqp.AMQPService;
import net.cloudengine.service.amqp.EndPoint;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.simple.workflow.entity.Transition;
import org.simple.workflow.services.WorkflowEngine;
import org.simple.workflow.services.WorkflowOperationException;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

	private Repository<Ticket,Long> ticketRepository;
	private Repository<Group, ObjectId> groupRepository;
	private SessionService sessionService;
	private AMQPService amqpService;
	private WorkflowEngine engine;
	
	@Autowired
	public TicketServiceImpl(RepositoryLocator repositoryLocator, SessionService sessionService, 
			AMQPService amqpService, WorkflowEngine engine) {
		super();
		this.ticketRepository = repositoryLocator.getRepository(Ticket.class);
		this.groupRepository = repositoryLocator.getRepository(Group.class);
		this.sessionService = sessionService;
		this.amqpService = amqpService;
		this.engine = engine;
	}

	@Override
	public Ticket createTicket() {
		User user = sessionService.getCurrentUser();
		Ticket ticket = new Ticket(user);
		ticketRepository.save(ticket);
		try {
			engine.start(ticket, user);
			WorkBook wb = ticket.getWorkBook();
			ticketRepository.save(wb);
			synchronizeInbox(ticket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticket;
	}

	@Override
	public Ticket get(Long id) {
		Ticket ticket = ticketRepository.get(id);
		if (ticket != null) {
			return ticket;
		}
		throw new IllegalArgumentException("Ticket does not exists");
	}

	@Override
	public boolean update(TicketModel ticketModel, DTOMapper mapper) {
		Ticket ticket = ticketRepository.get(ticketModel.getId());
		User user = sessionService.getCurrentUser();
		if (ticket != null && ticket.canMakeFullUpdate(user)) {
			mapper.fillEntity(ticketModel, ticket);
			synchronizeInbox(ticket);
			return true;
		} else if (ticket.canMakeParcialUpdate(user)) {
			WorkBook ws = ticket.getWorkBook();
			mapper.fillEntity(ticketModel.getWorkBook(), ws);
			synchronizeInbox(ticket);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean assignToMe(Long ticketId, boolean force) {
		Ticket ticket = ticketRepository.get(ticketId);
		if (ticket != null && ticket.getWorkBook() != null) {
			WorkBook wb = ticket.getWorkBook();
			String assignedUser = wb.getAssignedUser();
			if (force || StringUtils.isBlank(assignedUser)) {
				wb.setAssignedUser(sessionService.getCurrentUser().getUsername());
				synchronizeInbox(ticket);
				return true;
			}
			return false;
		}
		throw new IllegalArgumentException("Ticket does not exists or group is not assigned to ticket");
	}
	
	@Override
	public void leaveTicket(Long ticketId) {
		Ticket ticket = ticketRepository.get(ticketId);
		if (ticket != null && ticket.getWorkBook() != null) {
			String username = sessionService.getCurrentUser().getUsername();
			WorkBook ws = ticket.getWorkBook();
			String assignedUser = ws.getAssignedUser();
			if (username.equals(assignedUser)) {
				ws.setAssignedUser(null);
				synchronizeInbox(ticket);
			}
		}
	}
	
	
	@Override
	public OperationResult exec(Long ticketId, String action) {
		Ticket ticket = ticketRepository.get(ticketId);
		if (ticket != null) {
			try {
				engine.exec(ticket, action, this.sessionService.getCurrentUser());
				synchronizeInbox(ticket);
			} catch (WorkflowOperationException e) {
				OperationResult result = new OperationResult();
				result.setCode(OperationResult.ERROR);
				result.setMessage(e.getMessage());
				return result;
			}
		} else {
			OperationResult result = new OperationResult();
			result.setCode(OperationResult.ERROR);
			result.setMessage("invalid ticket");
			return result;
		}
		OperationResult result = new OperationResult();
		result.setCode(OperationResult.OK);
		return result;
	}

	@Override
	public OperationResult fork(Long ticketId, String action, List<String> groupIds) {
		//TODO mejorar
		List<Group> groups = new ArrayList<Group>();
		for(String groupId : groupIds) {
			groups.add(groupRepository.get(new ObjectId(groupId)));
		}
		Ticket ticket = ticketRepository.get(ticketId);
		if (ticket != null) {
			Group gs[] = groups.toArray(new Group[groups.size()]);
			try {
				engine.fork(ticket, action, this.sessionService.getCurrentUser(), gs);
				for(Group group : gs) {
					WorkBook workbook = ticket.getWorkBook(group);
					ticketRepository.save(workbook);
				}
				synchronizeInbox(ticket);
			} catch (WorkflowOperationException e) {
				OperationResult result = new OperationResult();
				result.setCode(OperationResult.ERROR);
				result.setMessage(e.getMessage());
				return result;
			}
		} else {
			OperationResult result = new OperationResult();
			result.setCode(OperationResult.ERROR);
			result.setMessage("invalid ticket");
			return result;
		}
		OperationResult result = new OperationResult();
		result.setCode(OperationResult.OK);
		return result;
	}
	
	@Override
	public List<Action> getActions(Long ticketId) {
		Ticket ticket = ticketRepository.get(ticketId);
		List<Action> actions = new ArrayList<Action>();
		if (ticket != null) {
			try {
				List<Transition> transitions = engine.getTransitions(ticket, sessionService.getCurrentUser());
				for(Transition transition : transitions) {
					Action action = new Action();
					action.setName(transition.getName());
					action.setDescription(transition.getDescription());
					action.setFork(transition.isForkTransition());
					actions.add(action);
				}
			} catch (WorkflowOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return actions;
	}

	private void synchronizeInbox(Ticket ticket) {
        try {
			this.amqpService.send(ticket.toJson(), EndPoint.TICKET_UPDATE);
		} catch (AmqpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
