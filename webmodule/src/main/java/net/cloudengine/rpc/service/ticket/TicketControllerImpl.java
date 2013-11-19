package net.cloudengine.rpc.service.ticket;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.jpa.dao.TicketDao;
import net.cloudengine.management.ExternalService;
import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.ticket.Ticket;
import net.cloudengine.model.ticket.WorkBook;
import net.cloudengine.model.ticket.WorkflowStep;
import net.cloudengine.rest.model.ticket.TicketModel;
import net.cloudengine.rpc.controller.ticket.TicketController;
import net.cloudengine.rpc.mappers.DTOMapper;
import net.cloudengine.rpc.mappers.MappersRegistry;
import net.cloudengine.service.web.SessionService;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ExternalService(exportedInterface=TicketController.class)
@Transactional
public class TicketControllerImpl implements TicketController {

	private TicketDao ticketDao;
	private Datastore<Group, ObjectId> groupDao;
	private SessionService sessionService;
	private MappersRegistry mappersRegistry;
	
	
	@Autowired
	public TicketControllerImpl(TicketDao ticketDao, MappersRegistry mappersRegistry, SessionService sessionService, 
			@Qualifier("groupStore") Datastore<Group, ObjectId> groupDao) {
		super();
		this.ticketDao = ticketDao;
		this.sessionService = sessionService;
		this.groupDao = groupDao;
		this.mappersRegistry = mappersRegistry;
	}

	@Override
	public TicketModel create() {
		DTOMapper mapper = mappersRegistry.getMapper(TicketModel.class);
		User user = sessionService.getCurrentUser();
		Ticket ticket = new Ticket(user);
		WorkBook wb = ticket.getWorkBook();
		WorkflowStep step = wb.startWorkflow(); 
		ticketDao.save(ticket);
		ticketDao.save(wb);
		ticketDao.save(step);
		return mapper.fillModel(ticket, TicketModel.class);
	}
	
	@Override
	public TicketModel get(Long id) {
		DTOMapper mapper = mappersRegistry.getMapper(TicketModel.class);
		Ticket ticket = ticketDao.get(id);
		if (ticket != null) {
			return mapper.fillModel(ticket, TicketModel.class);
		}
		throw new IllegalArgumentException("Ticket does not exists");
	}

	@Override
	public boolean update(TicketModel ticketModel) {
		DTOMapper mapper = mappersRegistry.getMapper(TicketModel.class);
		Ticket ticket = ticketDao.get(ticketModel.getId());
		User user = sessionService.getCurrentUser();
		if (ticket != null && ticket.canMakeFullUpdate(user)) {
			mapper.fillEntity(ticketModel, ticket);
			return true;
		} else if (ticket.canMakeParcialUpdate(user)) {
			WorkBook ws = ticket.getWorkBook();
			mapper.fillEntity(ticketModel.getWorkBook(), ws);
			return true;
		}
		return false;
	}
	
	@Override
	public void joinGroup(Long ticketId, String groupId) {
		Ticket ticket = ticketDao.get(ticketId);
		Group group = groupDao.get(new ObjectId(groupId));
		if (ticket != null && group != null) {
			WorkBook wb = ticket.createWorkBook(group);
			ticketDao.save(wb);
			return;
		}
		throw new IllegalArgumentException("Ticket or group does not exists");
	}
	
	@Override
	public boolean assignToMe(Long ticketId, boolean force) {
		Ticket ticket = ticketDao.get(ticketId);
		if (ticket != null && ticket.getWorkBook() != null) {
			WorkBook wb = ticket.getWorkBook();
			String assignedUser = wb.getAssignedUser();
			if (force || StringUtils.isBlank(assignedUser)) {
				wb.setAssignedUser(sessionService.getCurrentUser().getUsername());
				return true;
			}
			return false;
		}
		throw new IllegalArgumentException("Ticket does not exists or group is not assigned to ticket");
	}
	
	@Override
	public void leaveTicket(Long ticketId) {
		Ticket ticket = ticketDao.get(ticketId);
		if (ticket != null && ticket.getWorkBook() != null) {
			String username = sessionService.getCurrentUser().getUsername();
			WorkBook ws = ticket.getWorkBook();
			String assignedUser = ws.getAssignedUser();
			if (username.equals(assignedUser)) {
				ws.setAssignedUser(null);
			}
		}
	}

}
