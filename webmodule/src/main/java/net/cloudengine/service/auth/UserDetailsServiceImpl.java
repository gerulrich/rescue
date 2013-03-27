package net.cloudengine.service.auth;

import java.util.List;

import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.User;
import net.cloudengine.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	private UserService userService;
	
	@Autowired
	public UserDetailsServiceImpl(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		Assert.notNull(username, "El parámetro username no puede ser null");
		try {
			User ud = userService.getByUsername(username);
			if (ud == null) {
				throw new UsernameNotFoundException("No matching account");
			}
			List<Permission> permissions = userService.getPermissionForUser(ud);
			ud.setPermissions(permissions);
			return ud;
		} catch (UsernameNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new UsernameNotFoundException("No matching account", e);
		}
	}

}