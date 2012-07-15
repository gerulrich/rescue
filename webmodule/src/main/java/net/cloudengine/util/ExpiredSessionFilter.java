package net.cloudengine.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @author German Ulrich
 *
 */
public class ExpiredSessionFilter extends GenericFilterBean {

    private static final String SESSION_TIMEOUT = "SESSION_TIMEOUT";
	
	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

		if (AjaxUtils.isAjaxRequest(request)) {
			try {
				Object authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null ) {
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
}