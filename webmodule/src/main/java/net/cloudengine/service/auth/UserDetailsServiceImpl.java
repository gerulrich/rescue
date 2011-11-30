package net.cloudengine.service.auth;

import net.cloudengine.model.auth.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	UserService userService;
	
	public UserDetailsServiceImpl(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		try {
			User ud = userService.getByUsername(username);
			if (ud == null) {
				throw new UsernameNotFoundException("No matching account");
			}
			return ud;
		} catch (UsernameNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new UsernameNotFoundException("No matching account", e);
		}
	}

}