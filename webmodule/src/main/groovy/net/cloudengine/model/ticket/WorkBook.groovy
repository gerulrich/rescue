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

import net.cloudengine.api.jpa.DynamicUpdate
import net.cloudengine.domain.dsl.workflow.Engine
import net.cloudengine.domain.dsl.workflow.State
import net.cloudengine.management.Inject
import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User
import net.cloudengine.model.config.Enviroment

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
	double workflowVersion;
	@OneToMany(mappedBy="workBooK", cascade = CascadeType.ALL)
	Collection<WorkflowStep> steps = new HashSet<WorkflowStep>();
	@Transient @Inject Engine engine;
	@Transient @Inject Enviroment enviroment;
	
	WorkBook() { }
	
	WorkBook(User currentUser, Group forGroup) {
		this.groupId = forGroup.getId().toString();
		this.workflowVersion = engine.getVersion();
	}
	
	WorkflowStep startWorkflow() {
		State firstState = engine.firstState();
		
		WorkflowStep step = new WorkflowStep();
		step.setDate(new Date());
		step.setDescription(firstState.getDescription());
		step.setName(firstState.getName());
		step.setUsername(enviroment.getUser().getUsername());
		step.setWorkBooK(this);
		return step;
		
	}

}
