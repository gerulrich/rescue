package net.cloudengine.service.auth;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.auth.User;

@Service
public class UserServiceImpl implements UserService {

	Datastore<User, ObjectId> ds;
	
	public UserServiceImpl(Datastore<User, ObjectId> ds) {
		super();
		this.ds = ds;
	}
	
	@Override
	public User get(Long id) {
		return new User();
	}
	
	@Override
	public User getByUsername(String username) {
		Query<User> q = ds.createQuery().field("username").eq(username);
		return q.get();
	}

}
