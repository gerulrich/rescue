package net.cloudengine.service;

import java.util.List;

import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;

public interface UserService {

	User get(ObjectId id);
	
	User getByUsername(String username);
	
	ObjectId addUser(User user, String password);
	
	List<Group> getGroups();
	
	long updateUser(User user);
	
	long deleteUser(ObjectId id);

}
