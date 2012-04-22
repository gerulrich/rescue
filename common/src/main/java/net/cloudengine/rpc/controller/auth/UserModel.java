package net.cloudengine.rpc.controller.auth;

import java.io.Serializable;

public class UserModel implements Serializable {

	private static final long serialVersionUID = 5772015535531556656L;
	private String displayName;
	private String username;

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	
}
