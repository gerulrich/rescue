package net.cloudengine.util;

import org.springframework.web.context.request.WebRequest;

public final class AjaxUtils {

	public static boolean isAjaxRequest(WebRequest webRequest) {
		String requestedWith = webRequest.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	private AjaxUtils() {}

}