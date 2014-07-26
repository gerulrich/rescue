package net.cloudengine.rpc.model;

import java.io.Serializable;

import net.cloudengine.rpc.mappers.DataObject;
import net.cloudengine.rpc.mappers.Value;
import net.cloudengine.rpc.mappers.transformers.DateToStringTransformer;

@DataObject
public class TicketViewModel implements Serializable {
	
	String assignedUser;
	String category;
	@Value(value="closingDate", transformer=DateToStringTransformer.class)
	String closingDate;
	@Value(value="creationDate", transformer=DateToStringTransformer.class)
	String creationDate;
	String creatorGroup;
	String creatorUser;
	String currentState;
	String groupId;
	String location;
	String priority;
	String ticketId;
	public String getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getClosingDate() {
		return closingDate;
	}
	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatorGroup() {
		return creatorGroup;
	}
	public void setCreatorGroup(String creatorGroup) {
		this.creatorGroup = creatorGroup;
	}
	public String getCreatorUser() {
		return creatorUser;
	}
	public void setCreatorUser(String creatorUser) {
		this.creatorUser = creatorUser;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

}
