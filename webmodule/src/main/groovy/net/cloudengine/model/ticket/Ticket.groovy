package net.cloudengine.model.ticket

import java.util.Collection;
import java.util.Date;

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany
import javax.persistence.PostLoad
import javax.persistence.Transient

import org.simple.workflow.entity.Agent;
import org.simple.workflow.entity.DistributionGroup;
import org.simple.workflow.entity.Workable;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import net.cloudengine.dao.jpa.impl.DynamicUpdate;
import net.cloudengine.management.Inject;
import net.cloudengine.model.auth.Group
import net.cloudengine.model.auth.User
import net.cloudengine.model.config.Environment

@Entity
@DynamicUpdate
@ToString
class Ticket implements Workable {

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ticket_id")
	@Id Long id;
	Date creationDate;
	Date closingDate;
	@Column(columnDefinition="TEXT")
	String text;
	String creatorUser;
	String creatorGroup;
	@Transient @Inject Environment environment;

	@OneToMany(mappedBy="ticket")
	Collection<WorkBook> workBooks = new HashSet<WorkBook>();

	@Transient private Map<String,WorkBook> workBookIndex;

	protected Ticket() { super(); };

	Ticket(User user) {
		this.creationDate = new Date();
		this.creatorUser = user.getUsername();
		this.creatorGroup = user.getGroup().getId().toString();
		this.workBookIndex = [:];
	}
	
	@Override
	public void addStep(Agent agent, DistributionGroup dg, String name, String description, Date date) {
		WorkBook workbook = null;
		if (!this.workBookIndex.containsKey(dg.getOID())) {
			workbook = createWorkBook(dg);
			if (agent.getDG().equals(dg)) {
				workbook.setAssignedUser(agent.getOID());
			}
		} else {
			workbook = this.workBookIndex.get(dg.getOID());
		}
		workbook.setCurrentState(description);
		WorkflowStep step = new WorkflowStep();
		step.setName(name);
		step.setDescription(description);
		step.setWorkBooK(workbook);
		step.setUsername(agent.getOID());
		step.setDate(date);
		workbook.getSteps().add(step);
	}

	@Override
	public String getKey() {
		return "ticket#"+id;
	}
	
	@Override
	public void completed() {
		this.closingDate = new Date();
	}

	@PostLoad
	public void indexWorkBooks() {
		if (workBookIndex == null) {
			workBookIndex = [:];
			workBooks.each() { workBookIndex.put(it.getGroupId(), it) };
		}
	}

	@Transient
	WorkBook getWorkBook() {
		return workBookIndex.get(environment.getGroupId());
	}
	
	@Transient
	WorkBook getWorkBook(Group group) {
		String groupId = group.getId().toString();
		return workBookIndex.get(groupId);
	}
	
	WorkBook createWorkBook() {
		WorkBook wb = createWorkBook(environment.getUser().getGroup());
		wb.setAssignedUser(environment.getUser().getUsername());
		return wb;
	}

	WorkBook createWorkBook(Group group) {
		String groupId = group.getId().toString();
		if (!this.workBookIndex.containsKey(groupId)) {
			WorkBook ws = new WorkBook(environment.getUser(), group);
			ws.setTicket(this);
			this.workBooks.add(ws);
			this.workBookIndex.put(groupId, ws);
		}
		return this.workBookIndex.get(groupId);
	}
	
	boolean canMakeFullUpdate(User user) {
		String groupId = user.getGroup().getId().toString();
		if (groupId.equals(this.creatorGroup)) {
			WorkBook ws = workBookIndex.get(groupId);
			return user.getUsername().equals(ws.getAssignedUser());
		}
		return false;
	}
	
	boolean canMakeParcialUpdate(User user) {
		String groupId = user.getGroup().getId().toString();
		if (this.workBookIndex.containsKey(groupId)) {
			WorkBook ws = workBookIndex.get(groupId);
			return user.getUsername().equals(ws.getAssignedUser());
		}
		return false;
	}
	
	String toJson() {
		JsonObject ticketJson = new JsonObject();
		ticketJson.add("id", this.id);
		ticketJson.add("creationDate", this.creationDate.getTime());
		if (this.closingDate != null) {
			ticketJson.add("closingDate", this.closingDate?.getTime());
		}
		ticketJson.add("creatorUser", this.creatorUser);
		ticketJson.add("creatorGroup", this.creatorGroup);
		
		ticketJson.add("location", "");
		ticketJson.add("lat", 0d);
		ticketJson.add("lon", 0d);
		
		JsonArray workBooksJsonArray = new JsonArray(); 
		ticketJson.add("workBooks", workBooksJsonArray);
		workBooks.each() { 
			JsonObject workBooksJson = new JsonObject();
			workBooksJson.add("groupId", it.getGroupId());
			workBooksJson.add("groupName", it.getGroupName());
			workBooksJson.add("assignedUser", it.getAssignedUser()?:"");
			workBooksJson.add("currentState", it.getCurrentState()?:"");
			
			workBooksJson.add("category", "");
			workBooksJson.add("priority", "");
			
			workBooksJsonArray.add(workBooksJson);
		};
		return ticketJson.toString();
	}	
}
