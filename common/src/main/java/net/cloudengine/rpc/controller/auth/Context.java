package net.cloudengine.rpc.controller.auth;

public interface Context {
	
	void setCurrentUser(UserModel user);
	UserModel getCurrentUser();

}
