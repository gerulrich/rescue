

package net.cloudengine.model.auth

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken

@Document(collection="rememberme_token")
class RememberMeToken {
	
	@Id ObjectId id;
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
