package net.cloudengine.rpc.service;

import java.util.List;

import net.cloudengine.management.ExternalService;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.ticket.TicketView;
import net.cloudengine.rpc.controller.ticket.InboxController;
import net.cloudengine.rpc.mappers.DTOMapper;
import net.cloudengine.rpc.mappers.MappersRegistry;
import net.cloudengine.rpc.model.Folder;
import net.cloudengine.rpc.model.FolderTab;
import net.cloudengine.rpc.model.TicketViewModel;
import net.cloudengine.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@ExternalService(exportedInterface=InboxController.class)
public class InboxControllerImpl implements InboxController {

	private MongoTemplate template;
	private SessionService service;
	private MappersRegistry mappersRegistry; 
	
	@Autowired
	public InboxControllerImpl(MongoTemplate template, SessionService service, MappersRegistry mappersRegistry) {
		super();
		this.template = template;
		this.service = service;
		this.mappersRegistry = mappersRegistry;
	}

	
	@Override
	public Folder<TicketViewModel> getQuick() {
		DTOMapper mapper = mappersRegistry.getMapper(TicketViewModel.class);
		User user = service.getCurrentUser();
		Query query = new Query();
		query.addCriteria(Criteria.where("groupId").is(user.getGroup().getOID()));
		query.addCriteria(Criteria.where("closingDate").exists(false));
		query.with(new Sort(Sort.Direction.DESC,"ticketId"));
		List<TicketView> tickets = template.find(query, TicketView.class);
		
		//	En curso
		//	Asignados a mi
		//	Sin asignar
		//	Cerrados recientemente
		
		Folder<TicketViewModel> folder = new Folder<TicketViewModel>();
		FolderTab<TicketViewModel> tab = new FolderTab<TicketViewModel>();
		tab.setName("En curso");
		tab.setElements(mapper.fillModels(tickets, TicketViewModel.class));
		folder.getTabs().add(tab);		
		
		return folder;
	}
	
	@Override
	public Folder<TicketViewModel> getFull() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
