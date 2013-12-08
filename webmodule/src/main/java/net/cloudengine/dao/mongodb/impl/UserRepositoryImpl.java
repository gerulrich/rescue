package net.cloudengine.dao.mongodb.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.cloudengine.dao.mongodb.MongoRepository;
import net.cloudengine.dao.mongodb.UserRepository;
import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.Role;
import net.cloudengine.model.auth.User;
import net.cloudengine.validation.Assert;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserRepositoryImpl extends MongoRepository<User, ObjectId> implements UserRepository {

	public UserRepositoryImpl(MongoTemplate mongoTemplate) {
		super(User.class, mongoTemplate);
	}
	
	@Override
	public User getByUsername(String username) {
		Assert.notNull(username, "El parámetro username no puede ser null");
		User user = mongoTemplate.findOne(query(where("username").is(username)), this.getType());
		if (user != null) {
			user.setPermissions(getPermissionForUser(user));
		}
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		Assert.notNull(username, "El parámetro username no puede ser null");
		try {
			User ud = getByUsername(username);
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
				Role role = mongoTemplate.findOne(query(where("name").is(roleName)), Role.class);
				if (role != null) {
					permission.addAll(role.getPermissions());
				}
			}
		}
		return new ArrayList<Permission>(permission);
	}
}
