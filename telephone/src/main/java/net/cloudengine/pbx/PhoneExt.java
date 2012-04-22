package net.cloudengine.pbx;

/**
 * Representa una extension (telofono) de la central telefonica.
 * @author German Ulrich.
 *
 */
public class PhoneExt implements Comparable<PhoneExt> {
	
	private String number;
	private Status status;
	private String type;
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		PhoneExt other = (PhoneExt) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(PhoneExt other) {
		return number.compareTo(other.getNumber());
	}
}
