package net.cloudengine.api.mongo.dao;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDao extends Datastore<User, ObjectId>, UserDetailsService {
	
	User getByUsername(String username);
	
}
