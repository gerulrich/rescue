package net.cloudengine.rpc.controller.auth;

import java.io.Serializable;

import net.cloudengine.rpc.model.DataObject;
import net.cloudengine.rpc.model.Value;

@DataObject
public class UserModel implements Serializable {

	private static final long serialVersionUID = 5772015535531556656L;
	private String displayName;
	private String username;
	
	@Value("account.phoneNumber")
	private String phoneNumber;

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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
