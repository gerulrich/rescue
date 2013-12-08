package net.cloudengine.service.auth;

import java.util.List;

import net.cloudengine.dao.mongodb.UserRepository;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;
import net.cloudengine.util.Cipher;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userDao;
	private Repository<Group, ObjectId> groupRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userDao, RepositoryLocator repositoryLocator) {
		super();
		this.userDao = userDao;
		this.groupRepository = repositoryLocator.getRepository(Group.class);
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
		return user.getId();
	}
	
	public List<Group> getGroups() {
		return this.groupRepository.getAll();
	}

	@Override
	public long updateUser(User user) {
		userDao.update(user);
		return 0;
	}

	@Override
	public long deleteUser(ObjectId id) {
		userDao.delete(id);
		return 0;
	}

}
