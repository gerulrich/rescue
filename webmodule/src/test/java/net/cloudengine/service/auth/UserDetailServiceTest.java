package net.cloudengine.service.auth;

import static org.mockito.Mockito.when;
import junit.framework.TestCase;
import net.cloudengine.model.auth.User;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailServiceTest extends TestCase {

	@Mock UserService userService;
	
	@Before
	public void prepareTest() {
		User user = new User();
		user.setUsername("test1@admin.com");
		
		when(userService.getByUsername("test1@admin.com")).thenReturn(user);
		when(userService.getByUsername("test2@admin.com")).thenReturn(null);
		when(userService.getByUsername("test3@admin.com")).thenThrow(new NullPointerException("NullPointer mediante test mocking"));
		
	}

	@Test
	@Ignore
	public void testLoadUserByUsername() {
//		UserDetailsService detailService = new UserDetailsServiceImpl(userService);
//		UserDetails user = detailService.loadUserByUsername("test1@admin.com");
//		
//		Assert.assertNotNull(user);
//		Assert.assertEquals("test1@admin.com", user.getUsername());
		
		//TODO validar la secuencia con el mock
	}
	
	@Test(expected=UsernameNotFoundException.class)
	@Ignore
	public void testLoadUserByUsernameFail() {
//		UserDetailsService detailService = new UserDetailsServiceImpl(userService);
//		detailService.loadUserByUsername("test2@admin.com");
	}
	
	@Test(expected=UsernameNotFoundException.class)
	@Ignore
	public void testLoadUserByUsernameFailUnexpectedly() {
//		UserDetailsService detailService = new UserDetailsServiceImpl(userService);
//		detailService.loadUserByUsername("test3@admin.com");
	}

}
