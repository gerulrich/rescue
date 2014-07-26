package net.cloudengine.management;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudengine.model.auth.User;
import net.cloudengine.service.SessionService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class DebugCookieInterceptor extends HandlerInterceptorAdapter {

	private static final String DEBUG_PERMISSION = "DEBUG";
	private static final String DEBUG_COOKIE = "X-Debug";
	private SessionService service;
	
	@Autowired
	public DebugCookieInterceptor(SessionService service) {
		super();
		this.service = service;
	}



	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView == null) {
			return;
		}
		User currentUser = service.getCurrentUser();
		Boolean debugMode = Boolean.FALSE;
		if (currentUser != null && currentUser.hasPermission(DEBUG_PERMISSION)) {
			Cookie debugCookie = getCookie(request.getCookies());
			if (debugCookie != null && StringUtils.isNotEmpty(debugCookie.getValue())) {
				debugMode = Boolean.valueOf(debugCookie.getValue());
			}
		}
		if (!modelAndView.getViewName().startsWith("redirect")) {
			modelAndView.addObject("debug", debugMode);
		}
	}
	
	private Cookie getCookie(Cookie cookies[]) {
		Cookie cookie = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(DEBUG_COOKIE)) {
					cookie = cookies[i];
				}
			}
		}		
		return cookie;
	}

}
