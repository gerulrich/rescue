package net.cloudengine.model.auth

import org.springframework.security.core.GrantedAuthority;

class Role implements GrantedAuthority {

	Long id;
	String name;
	String description;
	
	@Override
	public String getAuthority() {
		return name;
	}	
}
