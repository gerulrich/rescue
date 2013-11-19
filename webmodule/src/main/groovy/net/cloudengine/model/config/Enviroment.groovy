package net.cloudengine.model.config

import net.cloudengine.model.auth.User

interface Enviroment {
	
	User getUser();
	String getGroupId();
	
}
