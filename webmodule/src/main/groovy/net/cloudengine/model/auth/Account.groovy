package net.cloudengine.model.auth

//import com.google.code.morphia.annotations.Converters;
//import com.google.code.morphia.annotations.Embedded;
//import com.google.code.morphia.annotations.Property;
//import com.google.code.morphia.converters.DateConverter;

//@Embedded
//@Converters(DateConverter.class)
class Account {

	Date accountExpirationDate;
	Date credentialsExpirationDate;
	boolean enabled = true;
	boolean locked = false;
	
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
