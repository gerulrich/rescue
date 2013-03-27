package net.cloudengine.new_.cti.model.asterisk;

import java.util.Date;

import net.cloudengine.new_.cti.model.QEntry;

public class QEntryAsteriskImpl implements QEntry {

	private String id;
	private String queue;
	private String callerId;
	private Date date;
	
	public QEntryAsteriskImpl(String id, String queue, String callerId, Date date) {
		super();
		this.id = id;
		this.callerId = callerId;
		this.date = date;
	}

	@Override
	public String getCallerId() {
		return callerId;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public String getQueue() {
		return queue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QEntryAsteriskImpl other = (QEntryAsteriskImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
