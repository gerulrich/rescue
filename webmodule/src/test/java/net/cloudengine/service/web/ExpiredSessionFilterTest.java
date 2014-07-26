package net.cloudengine.service.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudengine.model.auth.User;
import net.cloudengine.service.SessionService;
import net.cloudengine.service.web.ExpiredSessionFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;

public class ExpiredSessionFilterTest {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain chain;
	private SessionService service;
	
	@Before
	public void setUp() {
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		chain = Mockito.mock(FilterChain.class);
		service = Mockito.mock(SessionService.class);
	}
	
	private void addUserToSession(SessionService mockService) {
		Mockito
			.when(mockService.getCurrentUser())
			.thenReturn(new User());
	}
	
	private void makeAjaxRequest(HttpServletRequest mockRequest) {
		Mockito
			.when(mockRequest.getHeader(Mockito.anyString()))
			.thenReturn("XMLHttpRequest");
	}
	
	@Test
	public void doFilter_withUserInSessionAndCommonRequest_shouldChainFilter() throws IOException, ServletException {
		addUserToSession(service);
		ExpiredSessionFilter filter = new ExpiredSessionFilter(service);
		filter.doFilter(request, response, chain);
		
		Mockito.verify(request).getHeader("X-Requested-With");
		Mockito.verify(chain).doFilter(request, response);

	}
	
	@Test
	public void doFilter_withUserInSessionAndAjaxRequest_shouldChainFilter() throws IOException, ServletException {
		addUserToSession(service);
		makeAjaxRequest(request);
		ExpiredSessionFilter filter = new ExpiredSessionFilter(service);
		filter.doFilter(request, response, chain);
		
		Mockito.verify(request).getHeader("X-Requested-With");
		Mockito.verify(service).getCurrentUser();
		Mockito.verify(chain).doFilter(request, response);

	}
	
	@Test
	public void doFilter_withoutUserInSessionAndCommonRequest_shouldChainFilter() throws IOException, ServletException {
		ExpiredSessionFilter filter = new ExpiredSessionFilter(service);
		filter.doFilter(request, response, chain);
		
		Mockito.verify(request).getHeader("X-Requested-With");
		Mockito.verify(chain).doFilter(request, response);

	}
	
	@Test
	public void doFilter_withoutUserInSessionAndAjaxRequest_shouldReturnForbidden_403() throws IOException, ServletException {
		makeAjaxRequest(request);
		ExpiredSessionFilter filter = new ExpiredSessionFilter(service);
		filter.doFilter(request, response, chain);
		
		Mockito.verify(request).getHeader("X-Requested-With");
		Mockito.verify(service).getCurrentUser();
		Mockito.verify(response).sendError(Mockito.eq(HttpServletResponse.SC_FORBIDDEN), Mockito.anyString());
		Mockito.verifyZeroInteractions(chain);
	}	
	
	@Test
	public void doFilter_withAccessExceptionAndAjaxRequest_shouldReturnForbidden_403() throws IOException, ServletException {
		Mockito
			.when(service.getCurrentUser())
			.thenThrow(new AccessDeniedException("exception"));
		makeAjaxRequest(request);
		ExpiredSessionFilter filter = new ExpiredSessionFilter(service);
		filter.doFilter(request, response, chain);
		
		Mockito.verify(request).getHeader("X-Requested-With");
		Mockito.verify(service).getCurrentUser();
		Mockito.verify(response).sendError(Mockito.eq(HttpServletResponse.SC_FORBIDDEN), Mockito.anyString());
		Mockito.verifyZeroInteractions(chain);
	}	

}
