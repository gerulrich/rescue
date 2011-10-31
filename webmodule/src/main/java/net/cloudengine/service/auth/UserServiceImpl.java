package net.cloudengine.service.auth;

import org.springframework.stereotype.Service;

import net.cloudengine.model.auth.User;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User get(Long id) {
		return new User();
	}

}
