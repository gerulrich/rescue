package net.cloudengine.service.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudengine.model.auth.User;
import net.cloudengine.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filtro que detecta si la sesion del usuario ha expirado.
 * Si el request es por ajax, y la sesion ha expirado, response con un 403.
 * @author German Ulrich
 *
 */
public class ExpiredSessionFilter extends GenericFilterBean {

    private static final String SESSION_TIMEOUT = "SESSION_TIMEOUT";
	
    private SessionService service;
    
	@Autowired
    public ExpiredSessionFilter(SessionService service) {
		super();
		this.service = service;
	}

	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

		if (isAjaxRequest(request)) {
			try {
				User user = service.getCurrentUser();
				if (user == null ) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, SESSION_TIMEOUT);
					return;
				}
			} catch (AccessDeniedException e) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, SESSION_TIMEOUT);
				return;
			}
        }
        chain.doFilter(request, response);
    }
	
	public boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}
}