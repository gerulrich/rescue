package net.cloudengine.service.auth;

import java.util.List;

import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;

public interface UserService {

//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	User get(ObjectId id);
	
	User getByUsername(String username);
	
	List<Permission> getPermissionForUser(User user);
	
	ObjectId addUser(User user, String password);
	
	long updateUser(User user);
	long deleteUser(ObjectId id);

}
