package net.cloudengine.service.web;

import net.cloudengine.model.auth.User;
import net.cloudengine.service.SessionService;
import net.cloudengine.service.impl.SessionServiceImpl;
import net.cloudengine.utils.TestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionServiceImplTest {

	@Before
	public void setUp() {
		// para que no quede estado para los diferentes tests.
		SecurityContextHolder.getContext().setAuthentication(null);
		RequestContextHolder.setRequestAttributes(null);
	}
	
	@Test
	public void testGetCurrentUser_withoutUserInSession() {
		SessionService service = new SessionServiceImpl();
		User user = service.getCurrentUser();
		Assert.assertNull(user);
	}
	
	@Test
	public void testGetCurrentUser_withtUserInSession() {
		User user = TestUtil.createInstanceUser("testuser");
        Authentication auth = new UsernamePasswordAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(auth);
		
		SessionService service = new SessionServiceImpl();
		Assert.assertNotNull(service.getCurrentUser());
		Assert.assertEquals(user, service.getCurrentUser());
	}
	
	@Test
	public void test_getSessionId_withUser() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		SessionService service = new SessionServiceImpl();
		String sessionId = service.getSessionId(TestUtil.createInstanceUser("testuser"));
		Assert.assertNotNull(sessionId);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test_getSessionId_withNullUser() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		SessionService service = new SessionServiceImpl();
		service.getSessionId(null);
	}
}
