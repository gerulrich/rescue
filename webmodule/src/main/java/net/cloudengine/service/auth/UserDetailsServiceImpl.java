package net.cloudengine.service.auth;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	Datastore<User, ObjectId> ds;
	
	public UserDetailsServiceImpl(Datastore<User, ObjectId> ds) {
		super();
		this.ds = ds;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		try {
			Query<User> q = ds.createQuery().field("username").eq(username);
			User ud = q.get();
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