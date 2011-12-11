package net.cloudengine.web.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filtro que se ejecuta al iniciar sesión y guarda el usuario en la sesiónpara que se pueda
 * obtener en la vista.
 * @author German Ulrich
 *
 */
public class UserSessionFilter extends GenericFilterBean {

    private static final String CURRENT_USER = "currentUser";
    private static final String DEVELOPMENT = "development";
	
	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        request.getSession().setAttribute(CURRENT_USER, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (System.getenv("VCAP_APPLICATION") != null) {
        	// se esta ejecutando en producción.
        	request.getSession().setAttribute(DEVELOPMENT, Boolean.FALSE);
        } else {
        	request.getSession().setAttribute(DEVELOPMENT, Boolean.TRUE);
        }
        
        
        chain.doFilter(request, res);
    }
}