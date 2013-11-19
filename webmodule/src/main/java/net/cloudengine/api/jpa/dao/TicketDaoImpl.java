package net.cloudengine.api.jpa.dao;

import net.cloudengine.api.jpa.JPADatastore;
import net.cloudengine.model.ticket.Ticket;

import org.springframework.stereotype.Repository;

@Repository
public class TicketDaoImpl extends JPADatastore<Ticket, Long> implements TicketDao {

	public TicketDaoImpl() {
		super(Ticket.class);
	}	
}
