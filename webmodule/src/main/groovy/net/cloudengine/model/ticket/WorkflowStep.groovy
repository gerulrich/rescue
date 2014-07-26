package net.cloudengine.model.ticket

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity

class WorkflowStep {

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;
	String name;
	String description;
	String username;
	Date date;
	@ManyToOne
	@JoinColumn(name="work_book")
	WorkBook workBooK;

}
