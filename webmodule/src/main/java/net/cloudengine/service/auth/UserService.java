package net.cloudengine.service.auth;

import org.springframework.security.access.prepost.PreAuthorize;

import net.cloudengine.model.auth.User;

public interface UserService {

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	User get(Long id);

}
