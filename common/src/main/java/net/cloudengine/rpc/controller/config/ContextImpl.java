package net.cloudengine.rpc.controller.config;

import net.cloudengine.rpc.controller.auth.Context;
import net.cloudengine.rpc.controller.auth.UserModel;

public class ContextImpl implements Context {

	private UserModel currentUser;
	
	public ContextImpl() {
		super();
	}
	
	public void setCurrentUser(UserModel user) {
		this.currentUser = user;
	}

	public UserModel getCurrentUser() {
		return currentUser;
	}

}
