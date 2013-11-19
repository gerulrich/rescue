package net.cloudengine.service.auth;

import java.util.List;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.mongo.dao.UserDao;
import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;
import net.cloudengine.util.Cipher;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private UserDao userDao;
	private Datastore<Group, ObjectId> groupStore;
	private XMPPService xmppService;
	
	@Autowired
	public UserServiceImpl(UserDao userDao, XMPPService xmppService, @Qualifier("groupStore") Datastore<Group, ObjectId> groupStore) {
		super();
		this.userDao = userDao;
		this.xmppService = xmppService;
		this.groupStore = groupStore;
	}

	@Override
	public User get(ObjectId id) {
		return userDao.get(id);
	}
	
	@Override
	public User getByUsername(String username) {
		return userDao.getByUsername(username);
	}
	
	@Override
	public ObjectId addUser(User user, String password) {
		user.setPassword(new Cipher().encrypt(password));
		userDao.save(user);
		xmppService.addUser(user.getUsername(), user.getPassword(), user.getDisplayName());
		return user.getId();
	}
	
	public List<Group> getGroups() {
		return this.groupStore.getAll();
	}

	@Override
	public long updateUser(User user) {
		userDao.update(user);
		xmppService.updateUser(user.getUsername(), user.getPassword(), user.getDisplayName());		
		return 0;
	}

	@Override
	public long deleteUser(ObjectId id) {
		User user = userDao.get(id);
		userDao.delete(id);
		xmppService.deleteUser(user.getUsername());
		return 0;
	}

}
