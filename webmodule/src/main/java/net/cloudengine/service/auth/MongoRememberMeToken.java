package net.cloudengine.service.auth;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("rememberme_token")
public class MongoRememberMeToken  {

		@Id
		private ObjectId id;
		private String username;
	    private String series;
	    private String tokenValue;
	    private Date date;
		
	    
	    public MongoRememberMeToken(String username, String series, String tokenValue, Date date) {
			super();
			this.username = username;
			this.series = series;
			this.tokenValue = tokenValue;
			this.date = date;
		}
	    
		public MongoRememberMeToken() {
			super();
		}
		
		public MongoRememberMeToken(PersistentRememberMeToken token) {
			this(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
		}
		
		public ObjectId getId() {
			return id;
		}
		public void setId(ObjectId id) {
			this.id = id;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getSeries() {
			return series;
		}
		public void setSeries(String series) {
			this.series = series;
		}
		public String getTokenValue() {
			return tokenValue;
		}
		public void setTokenValue(String tokenValue) {
			this.tokenValue = tokenValue;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		
		PersistentRememberMeToken toSpringToken() {
			return new PersistentRememberMeToken(username, series, tokenValue, date);
		}
	}