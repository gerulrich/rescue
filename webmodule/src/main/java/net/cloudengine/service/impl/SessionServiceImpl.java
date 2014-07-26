package net.cloudengine.service.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpSession;

import net.cloudengine.model.auth.User;
import net.cloudengine.model.config.Environment;
import net.cloudengine.service.SessionService;
import net.cloudengine.validation.Assert;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class SessionServiceImpl implements SessionService, Environment {

	private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
	private ConcurrentMap<String, Queue<User>> groups = new ConcurrentHashMap<String, Queue<User>>();

	@Override
	public String getSessionId(User user) {
		Assert.notNull(user, "user cannot be null");
		
		UsernamePasswordAuthenticationToken ut = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContext sctx = SecurityContextHolder.createEmptyContext(); 
		sctx.setAuthentication(ut);
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute(SPRING_SECURITY_CONTEXT, sctx);
		String sessionId = session.getId();
		putUserInQueue(user);
		return sessionId;
	}

	@Override
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return (User)authentication.getPrincipal();
		}
		return null;
	}

	@Override
	public User getUser() {
		return getCurrentUser();
	}

	@Override
	public String getGroupId() {
		return getUser().getGroup().getId().toString();
	}
	
	public void putUserInQueue(User user) {
		String groupId = user.getGroup().getId().toString();
		if (!groups.containsKey(groupId)) {
			groups.put(groupId, new ConcurrentLinkedQueue<User>());
		}
		groups.get(groupId).add(user);
	}
	
	public void removeUserInQueue(User user) {
		
	}
	
//	public User getNextUser(Group group) {
//		String groupId = group.getId().toString();
//		if (groups.containsKey(groupId)) {
//			Queue<User> users = groups.get(groupId);
//			User selectedUser = users.poll();
//			if (selectedUser != null) {
//				users.add(selectedUser);
//			}
//			return selectedUser;
//		}
//		return null;
//	}

//	@Override
//	public void sessionCreated(HttpSessionEvent arg0) {
//		// TODO Auto-generated method stub
//	}

//	@Override
//	public void sessionDestroyed(HttpSessionEvent event) {
//		HttpSession session = event.getSession();
//		SecurityContext sctx = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
//		if (sctx != null) {
//			UsernamePasswordAuthenticationToken ut = (UsernamePasswordAuthenticationToken) sctx.getAuthentication();
//			User user = (User) ut.getPrincipal();
//			removeUserInQueue(user);
//		}		
//	}
	
	
	
	

}
