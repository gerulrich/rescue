package net.cloudengine.api.mongo.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.api.mongo.MongoDBWrapper;
import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.Role;
import net.cloudengine.model.auth.User;
import net.cloudengine.validation.Assert;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.code.morphia.Morphia;

public class UserDaoImpl extends MongoStore<User, ObjectId> implements UserDao {

	private Datastore<Role, ObjectId> roleStore;
	
	public UserDaoImpl(Datastore<Role, ObjectId> roleStore, MongoDBWrapper wrapper, Morphia morphia) {
		super(wrapper, User.class, morphia);
		this.roleStore = roleStore;
	}
	
	@Override
	public User getByUsername(String username) {
		Assert.notNull(username, "El parámetro username no puede ser null");
		Query<User> query = createQuery().field("username").eq(username);
		User user = query.get();
		if (user != null) {
			user.setPermissions(getPermissionForUser(user));
		}
		return user;
	}



	@Override
	public UserDetails loadUserByUsername(String username) {
		Assert.notNull(username, "El parámetro username no puede ser null");
		try {
			Query<User> query = createQuery().field("username").eq(username);
			User ud = query.get();
			if (ud == null) {
				throw new UsernameNotFoundException("No matching account");
			}
			ud.setPermissions(getPermissionForUser(ud));
			return ud;
		} catch (UsernameNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new UsernameNotFoundException("No matching account", e);
		}
	}
	
	private List<Permission> getPermissionForUser(User user) {
		Set<Permission> permission = new HashSet<Permission>();
		String roleString = user.getRoles();
		if (StringUtils.isNotBlank(roleString)) {
			String r[] = roleString.split(",");
			for(String roleName : r) {
				Role role = roleStore.createQuery().field("name").eq(roleName).get();
				if (role != null) {
					permission.addAll(role.getPermissions());
				}
			}
		}
		return new ArrayList<Permission>(permission);
	}

}
