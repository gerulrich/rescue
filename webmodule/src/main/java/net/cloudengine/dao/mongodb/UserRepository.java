package net.cloudengine.dao.mongodb;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserRepository extends Repository<User, ObjectId>, UserDetailsService {
	
	User getByUsername(String username);
	
}
