package net.cloudengine.util;

import javax.servlet.http.HttpSession;

import net.cloudengine.model.auth.User;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class UserUtil {
	
	private static UserUtil instance = new UserUtil();
	
	private UserUtil() {
		
	}
	
	public static UserUtil getInstance() {
		return instance;
	}
	
	public User currentUser() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession(false);
	    if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
	    	SecurityContext sctx = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
	    	User user = (User) sctx.getAuthentication().getPrincipal();
	    	return user;
	    }
		return null;
	}

}
