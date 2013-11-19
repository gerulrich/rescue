package net.cloudengine.rest.model.ticket;

import java.io.Serializable;

import net.cloudengine.rpc.mappers.DataObject;
import net.cloudengine.rpc.mappers.ReadOnly;
import net.cloudengine.rpc.mappers.Value;
import net.cloudengine.rpc.mappers.transformers.DateToStringTransformer;

@DataObject
public class TicketModel implements Serializable {
	
	private Long id;
	private String text;
	@ReadOnly
	private String creatorUser;
	@ReadOnly
	@Value(value="creationDate", transformer=DateToStringTransformer.class)
	private String creationDate;
	private WorkBookModel workBook;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(String creatorUser) {
		this.creatorUser = creatorUser;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public WorkBookModel getWorkBook() {
		return workBook;
	}

	public void setWorkBook(WorkBookModel workBook) {
		this.workBook = workBook;
	}
}
