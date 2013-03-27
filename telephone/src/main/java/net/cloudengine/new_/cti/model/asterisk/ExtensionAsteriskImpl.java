package net.cloudengine.new_.cti.model.asterisk;

import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.Status;


public class ExtensionAsteriskImpl implements Extension {
	
	private String number;
	private Status status;
	private String type;
	
	public ExtensionAsteriskImpl(String number, Status status, String type) {
		super();
		this.number = number;
		this.status = status;
		this.type = type;
	}
	
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
		ExtensionAsteriskImpl other = (ExtensionAsteriskImpl) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

}
