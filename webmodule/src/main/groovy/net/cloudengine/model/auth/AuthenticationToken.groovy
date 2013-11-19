package net.cloudengine.model.auth

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id;

@Entity(value="auth_token", noClassnameStored=true)
class AuthenticationToken {
	
	@Id
	ObjectId id;
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
