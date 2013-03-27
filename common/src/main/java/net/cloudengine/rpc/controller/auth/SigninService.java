package net.cloudengine.rpc.controller.auth;

public interface SigninService {
	
	String login(String username, String password);
	
	String getAuthToken(String username, String password);
	
	public UserModel getUserDetail();
	
	void logout(UserModel user);

}
