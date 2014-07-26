package net.cloudengine.model.ticket

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Transient

import net.cloudengine.dao.jpa.impl.DynamicUpdate
import net.cloudengine.management.Inject
import net.cloudengine.model.auth.Group
import net.cloudengine.model.auth.User
import net.cloudengine.model.config.Environment

@Entity
@DynamicUpdate
class WorkBook {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;
	@ManyToOne
	@JoinColumn(name="ticket_id")
	Ticket ticket;
	String groupId;
	String assignedUser;
	String groupName;
	String currentState;
	@OneToMany(mappedBy="workBooK", cascade = CascadeType.ALL)
	Collection<WorkflowStep> steps = new HashSet<WorkflowStep>();
	
	protected WorkBook() { super(); }
	
	WorkBook(User currentUser, Group forGroup) {
		this.groupId = forGroup.getId().toString();
		this.groupName = forGroup.getName();
	}
	
}