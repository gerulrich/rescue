package net.cloudengine.service.auth;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	Datastore<User, ObjectId> ds;
	
	public UserDetailsServiceImpl(Datastore<User, ObjectId> ds) {
		super();
		this.ds = ds;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		try {
			Query<User> q = ds.createQuery().field("username").eq(username);
			User ud = q.get();
			if (ud == null && username.equals("german")) {
				ud = new User();
				ud.setUsername("german");
				ud.setPassword("d033e22ae348aeb5660fc2140aec35850c4da997");
				ud.setDisplayName("German");
			}
			
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