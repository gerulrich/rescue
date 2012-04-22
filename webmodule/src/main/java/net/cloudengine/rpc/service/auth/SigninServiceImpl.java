package net.cloudengine.rpc.service.auth;

import javax.servlet.http.HttpSession;

import net.cloudengine.model.auth.User;
import net.cloudengine.rpc.controller.auth.SigninService;
import net.cloudengine.rpc.controller.auth.UserModel;
import net.cloudengine.service.auth.UserService;
import net.cloudengine.util.Cipher;
import net.cloudengine.util.ExternalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@ExternalService(exportedInterface=SigninService.class)
public class SigninServiceImpl implements SigninService {

	private UserService service;
	
	@Autowired
	public void setService(UserService service) {
		this.service = service;
	}

	@Override
	public String login(String username, String password) {
		User user = service.getByUsername(username);
		if (user != null && password != null) {
			boolean aut = new Cipher().encrypt(password).equals(user.getPassword());
			if (!aut) {
				return null;
			} else {
				UsernamePasswordAuthenticationToken ut = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
				SecurityContext sctx = SecurityContextHolder.createEmptyContext(); 
		        sctx.setAuthentication(ut);
		        
		        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			    HttpSession session = attr.getRequest().getSession(true);
			    String sessionId = session.getId();
			    session.setAttribute("SPRING_SECURITY_CONTEXT", sctx);

				return sessionId;
			}
		}
		return null;
	}
	
	public UserModel getUserDetail() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession(false);
	    if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
	    	SecurityContext sctx = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
	    	User user = (User) sctx.getAuthentication().getPrincipal();
	    	
	    	UserModel model = new UserModel();
	    	model.setDisplayName(user.getDisplayName());
	    	model.setUsername(user.getUsername());
	    	
	    	return model;
	    	
	    }	    
	    return null;		
	}
	

	@Override
	public void logout(UserModel user) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession(false);
	    if (session != null) {
	    	session.invalidate();
	    }
	}

}
