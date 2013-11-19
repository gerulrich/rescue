

package net.cloudengine.model.auth

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("rememberme_token")
class RememberMeToken {
	
	@Id
	ObjectId id;
	String username;
	String series;
	String tokenValue;
	Date date;
	
	RememberMeToken(String username, String series, String tokenValue, Date date) {
		super();
		this.username = username;
		this.series = series;
		this.tokenValue = tokenValue;
		this.date = date;
	}
	
	RememberMeToken() {
		super();
	}
	
	RememberMeToken(PersistentRememberMeToken token) {
		this(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
	}
	
	PersistentRememberMeToken toSpringToken() {
		return new PersistentRememberMeToken(username, series, tokenValue, date);
	}

}
