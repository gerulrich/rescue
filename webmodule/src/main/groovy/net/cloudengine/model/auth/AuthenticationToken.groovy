package net.cloudengine.model.auth

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="auth_token")
class AuthenticationToken {
	
	@Id ObjectId id;
	String username;
	String series;
	String tokenValue;
	Date date;
	
	public AuthenticationToken(String username, String series, String tokenValue, Date date) {
		super();
		this.username = username;
		this.series = series;
		this.tokenValue = tokenValue;
		this.date = date;
	}
	
	public AuthenticationToken() {
		
	}
	
	

}
