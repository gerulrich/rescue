package net.cloudengine.rpc.service.auth;

import javax.servlet.http.HttpSession;

import net.cloudengine.management.ExternalService;
import net.cloudengine.model.auth.User;
import net.cloudengine.rpc.controller.auth.SigninService;
import net.cloudengine.rpc.controller.auth.UserModel;
import net.cloudengine.rpc.mappers.DTOMapper;
import net.cloudengine.rpc.mappers.MappersRegistry;
import net.cloudengine.service.auth.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@ExternalService(exportedInterface=SigninService.class)
public class SigninServiceImpl implements SigninService {

	private MappersRegistry mappersRegistry;
	private AuthenticationService authService;
	
	
	@Autowired
	public void setMappersRegistry(MappersRegistry mappersRegistry) {
		this.mappersRegistry = mappersRegistry;
	}
	
	@Autowired
	public void setAuthService(AuthenticationService authService) {
		this.authService = authService;
	}

	@Override
	public String login(String username, String password) {
		return authService.login(username, password);
	}
	
	public UserModel getUserDetail() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession(false);
	    if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
	    	SecurityContext sctx = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
	    	User user = (User) sctx.getAuthentication().getPrincipal();
	    	DTOMapper userMapper = mappersRegistry.getMapper(UserModel.class);
	    	return userMapper.fillModel(user, UserModel.class);
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
