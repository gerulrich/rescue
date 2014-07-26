package net.cloudengine.model.ticket

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="open_ticket")
class TicketView {

	@Id
	ObjectId id;
	String assignedUser;
	String category;
	Date closingDate;
	Date creationDate;
	String creatorGroup;
	String creatorUser;
	String currentState;
	String groupId;
	String location;
	String priority;
	String ticketId;
}
