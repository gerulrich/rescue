package net.cloudengine.rest.model.ticket;

import java.io.Serializable;

import net.cloudengine.rpc.mappers.DataObject;
import net.cloudengine.rpc.mappers.Id;
import net.cloudengine.rpc.mappers.ReadOnly;

@DataObject
public class WorkBookModel implements Serializable {
	
	@Id private Long id;
	@ReadOnly
	private String assignedUser;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}	
}
