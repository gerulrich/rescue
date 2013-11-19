package net.cloudengine.model.ticket

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.PostLoad
import javax.persistence.Transient

import net.cloudengine.api.jpa.DynamicUpdate
import net.cloudengine.management.Inject;
import net.cloudengine.model.auth.Group
import net.cloudengine.model.auth.User
import net.cloudengine.model.config.Enviroment

@Entity
@DynamicUpdate
@ToString
class Ticket {

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ticket_id")
	@Id Long id;
	Date creationDate;
	Date closingDate;
	@Column(columnDefinition="TEXT")
	String text;
	String creatorUser;
	String creatorGroup;
	@Transient @Inject Enviroment enviroment;

	@OneToMany(mappedBy="ticket")
	Collection<WorkBook> workBooks = new HashSet<WorkBook>();

	@Transient private Map<String,WorkBook> workBookIndex;

	protected Ticket() { };

	Ticket(User user) {
		this.creationDate = new Date();
		this.creatorUser = user.getUsername();
		this.creatorGroup = user.getGroup().getId().toString();
		this.workBookIndex = [:];
		this.createWorkBook();
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
		return workBookIndex.get(enviroment.getGroupId());
	}
	
	WorkBook createWorkBook() {
		WorkBook wb = createWorkBook(enviroment.getUser().getGroup());
		wb.setAssignedUser(enviroment.getUser().getUsername());
		return wb;
	}

	WorkBook createWorkBook(Group group) {
		String groupId = group.getId().toString();
		if (!this.workBookIndex.containsKey(groupId)) {
			WorkBook ws = new WorkBook(enviroment.getUser(), group);
			ws.setTicket(this);
			this.workBooks.add(ws);
			this.workBookIndex.put(groupId, ws);
		}
		this.workBookIndex.get(groupId);
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
}
