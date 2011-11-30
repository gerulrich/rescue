package net.cloudengine.service.auth;

import static org.mockito.Mockito.when;
import net.cloudengine.model.auth.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailServiceTest {

	@Mock UserService userService;
	
	@Before
	public void prepareTest() {
		User user = new User();
		
		when(userService.getByUsername("test1@admin.com")).thenReturn(user);
		when(userService.getByUsername("test2@admin.com")).thenReturn(null);
		
	}

	@Test
	public void testLoadUserByUsername() {
		UserDetailsService detailService = new UserDetailsServiceImpl(userService);
		UserDetails user = detailService.loadUserByUsername("test1@admin.com");
	}
	
	@Test(expected=UsernameNotFoundException.class)
	public void testLoadUserByUsernameFail() {
		UserDetailsService detailService = new UserDetailsServiceImpl(userService);
		detailService.loadUserByUsername("test2@admin.com");
	}

}
