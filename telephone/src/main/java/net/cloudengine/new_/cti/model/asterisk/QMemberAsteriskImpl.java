package net.cloudengine.new_.cti.model.asterisk;

import net.cloudengine.new_.cti.model.QMember;

public class QMemberAsteriskImpl implements QMember {

	private String number;
	private String queue;
	
	public QMemberAsteriskImpl(String number, String queue) {
		super();
		this.number = number;
	}

	@Override
	public String getNumber() {
		return number;
	}
	
	@Override
	public String getQueue() {
		return queue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
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
		QMemberAsteriskImpl other = (QMemberAsteriskImpl) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		return true;
	}
}
