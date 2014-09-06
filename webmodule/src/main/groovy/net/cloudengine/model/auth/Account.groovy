package net.cloudengine.model.auth

import net.cloudengine.web.crud.support.CrudProperty

class Account implements Serializable {

	Date accountExpirationDate;
	Date credentialsExpirationDate;
	@CrudProperty
	boolean enabled = true;
	boolean locked = false;
	String agentNumber;
	String agentPassword;
	
	def isAccountExpired() {
		if (accountExpirationDate == null) {
			return false;
		} else {
			return ( new Date() >= accountExpirationDate );
		}
	}
	
	def isCredentialsExpired() {
		if (credentialsExpirationDate == null) {
			return false;
		} else {
			return ( new Date() >= credentialsExpirationDate );
		}
	}
	
}
