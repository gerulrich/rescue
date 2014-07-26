package net.cloudengine.model.config

import net.cloudengine.model.auth.User

interface Environment {
	
	User getUser();
	String getGroupId();
	
}
