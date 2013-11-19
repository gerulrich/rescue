package net.cloudengine.service.auth;

import java.util.UUID;

import net.cloudengine.model.auth.User;
import net.cloudengine.service.web.SessionService;
import net.cloudengine.utils.TestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AuthenticationServiceTest {

	private UserService userService;
	private SessionService sessionService;
	
	@Before
	public void init() {
		userService = TestUtil.getUserServiceMock();
		sessionService = Mockito.mock(SessionService.class);
		Mockito
			.when(sessionService.getSessionId(Mockito.any(User.class)))
			.thenAnswer(new Answer<String>() {

				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					return UUID.randomUUID().toString().replaceAll("-", "");
				}
			});
	}
	
	@Test
	@Ignore
	public void testLoginWithValidUserWithAccessPermission() throws Exception {
		String username = "user1@test.com";
		AuthenticationService authService = new AuthenticationServiceImpl(userService, sessionService, null);
		String token = authService.login(username, "pass1");
		
		Assert.assertNotNull(token);
		
		Mockito.verify(userService).getByUsername(username);
		
		ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
//		Mockito.verify(userService).getPermissionForUser(argument.capture());
		Assert.assertEquals(username, argument.getValue().getUsername());
		Mockito.verifyNoMoreInteractions(userService);
		
		Mockito.verify(sessionService).getSessionId(Mockito.eq(argument.getValue()));
	}
	
	@Test(expected=RuntimeException.class)
	public void testLoginWithValidUserWithoutAccessPermission() throws Exception {
		String username = "user2@test.com";
		AuthenticationService authService = new AuthenticationServiceImpl(userService, sessionService, null);
		authService.login(username, "pass2");
		// nada para validar, se lanza una excepcion.
	}
	
	@Test
	public void testLoginWithInvalidUser() throws Exception {
		String username = "user2@test.com";
		AuthenticationService authService = new AuthenticationServiceImpl(userService, sessionService, null);
		String token = authService.login(username, "invalidpass");
		
		Assert.assertNull(token);
		
		Mockito.verify(userService).getByUsername(username);
		Mockito.verifyNoMoreInteractions(userService);
		Mockito.verifyZeroInteractions(sessionService);
	}
	
	@Test
	public void testLoginWithoutUsername() throws Exception {
		AuthenticationService authService = new AuthenticationServiceImpl(userService, sessionService, null);
		String token = authService.login(null, "pass");
		Assert.assertNull(token);
		
		Mockito.verify(userService).getByUsername(null);
		Mockito.verifyNoMoreInteractions(userService);
		Mockito.verifyZeroInteractions(sessionService);
	}
	
	@Test
	public void testLoginWithoutPassword() throws Exception {
		String username = "user1@test.com";
		AuthenticationService authService = new AuthenticationServiceImpl(userService, sessionService, null);
		String token = authService.login("user1@test.com", null);
		Assert.assertNull(token);
		
		Mockito.verify(userService).getByUsername(username);
		Mockito.verifyNoMoreInteractions(userService);
		Mockito.verifyZeroInteractions(sessionService);
	}
	
	@Test
	public void testLoginWithoutUsernameAndPassword() throws Exception {
		AuthenticationService authService = new AuthenticationServiceImpl(userService, sessionService, null);
		String token = authService.login(null, null);
		Assert.assertNull(token);
		
		Mockito.verify(userService).getByUsername(null);
		Mockito.verifyNoMoreInteractions(userService);
		Mockito.verifyZeroInteractions(sessionService);
	}

}
