package net.cloudengine.service.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.Role;
import net.cloudengine.model.auth.User;
import net.cloudengine.util.Cipher;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Resource(name="userStore")
	private Datastore<User, ObjectId> ds;
	
	@Resource(name="roleStore")
	private Datastore<Role, ObjectId> roleStore;
	
	private XMPPService xmppService;
	
	public void setDs(Datastore<User, ObjectId> ds) {
		this.ds = ds;
	}
	
	public void setRoleStore(Datastore<Role, ObjectId> roleStore) {
		this.roleStore = roleStore;
	}
	
	@Autowired
	public void setXmppService(XMPPService xmppService) {
		this.xmppService = xmppService;
	}

	@Override
	public User get(ObjectId id) {
		return ds.get(id);
	}
	
	@Override
	public User getByUsername(String username) {
		Query<User> q = ds.createQuery().field("username").eq(username);
		return q.get();
	}
	
	@Override
	public List<Permission> getPermissionForUser(User user) {
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

	@Override
	public ObjectId addUser(User user, String password) {
		user.setPassword(new Cipher().encrypt(password));
		ds.save(user);
		xmppService.addUser(user.getUsername(), user.getPassword(), user.getDisplayName());
		return user.getId();
	}

	@Override
	public long updateUser(User user) {
		ds.update(user);
		xmppService.updateUser(user.getUsername(), user.getPassword(), user.getDisplayName());		
		return 0;
	}

	@Override
	public long deleteUser(ObjectId id) {
		User user = ds.get(id);
		ds.delete(id);
		xmppService.deleteUser(user.getUsername());
		return 0;
	}
	
	
	

}
