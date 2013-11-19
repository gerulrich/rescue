package net.cloudengine.service.web;

import static org.mockito.Mockito.*;
import net.cloudengine.model.auth.User;
import net.cloudengine.service.web.CurrentUser;
import net.cloudengine.service.web.CurrentUserArgumentResolver;
import net.cloudengine.service.web.SessionService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;

public class CurrentUserArgumentResolverTest {

	private SessionService sessionService;
	private MethodParameter parameter;
	
	@Before
	public void setUp() {
		sessionService = mock(SessionService.class);
		when(sessionService.getCurrentUser())
			.thenReturn(new User());
		parameter = mock(MethodParameter.class);
	}
	
	@Test
	public void testResolveArgument_withAnnotatedMethod() throws Exception {
		CurrentUser annotation = mock(CurrentUser.class);
		when(parameter.getParameterAnnotation(Mockito.eq(CurrentUser.class)))
			.thenReturn(annotation);
		
		WebArgumentResolver resolver = new CurrentUserArgumentResolver(sessionService);
		Object output = resolver.resolveArgument(parameter, null);
		Assert.assertNotNull(output);
		Assert.assertEquals(User.class, output.getClass());
		verify(parameter).getParameterAnnotation(Mockito.eq(CurrentUser.class));
		verifyNoMoreInteractions(parameter);
	}
	
	@Test
	public void testResolveArgument_withoutAnnotatedMethod() throws Exception {
		WebArgumentResolver resolver = new CurrentUserArgumentResolver(sessionService);
		Object output = resolver.resolveArgument(parameter, null);
		Assert.assertNotNull(output);
		Assert.assertEquals(WebArgumentResolver.UNRESOLVED, output);
		verify(parameter).getParameterAnnotation(Mockito.eq(CurrentUser.class));
		verifyNoMoreInteractions(parameter);
	}

}
