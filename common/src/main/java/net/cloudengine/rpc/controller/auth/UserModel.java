package net.cloudengine.rpc.controller.auth;

import java.io.Serializable;

import net.cloudengine.rpc.model.DataObject;
import net.cloudengine.rpc.model.Value;

@DataObject
public class UserModel implements Serializable {

	private static final long serialVersionUID = 5772015535531556656L;
	private String displayName;
	private String username;
	
	@Value("account.agentNumber")
	private String agentNumber;
	
	@Value("account.agentPassword")
	private String agentPassword;

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
	public String getAgentNumber() {
		return agentNumber;
	}
	public void setAgentNumber(String agentNumber) {
		this.agentNumber = agentNumber;
	}
	public String getAgentPassword() {
		return agentPassword;
	}
	public void setAgentPassword(String agentPassword) {
		this.agentPassword = agentPassword;
	}
}
