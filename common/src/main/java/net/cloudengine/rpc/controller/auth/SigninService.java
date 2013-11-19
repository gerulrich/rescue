package net.cloudengine.rpc.controller.auth;

public interface SigninService {
	
	String login(String username, String password);
	
	UserModel getUserDetail();
	
	void logout(UserModel user);

}
