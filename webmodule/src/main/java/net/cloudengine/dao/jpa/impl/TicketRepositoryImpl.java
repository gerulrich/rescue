package net.cloudengine.dao.jpa.impl;

import net.cloudengine.dao.jpa.TicketRepository;
import net.cloudengine.model.ticket.Ticket;

import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryImpl extends JPARepository<Ticket, Long> implements TicketRepository {

	public TicketRepositoryImpl() {
		super(Ticket.class);
	}	
}
