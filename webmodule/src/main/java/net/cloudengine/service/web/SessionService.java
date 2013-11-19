package net.cloudengine.service.web;

import net.cloudengine.model.auth.User;

public interface SessionService {
	
	String getSessionId(User user);
	
	User getCurrentUser();

}
