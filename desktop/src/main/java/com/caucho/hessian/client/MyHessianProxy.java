package com.caucho.hessian.client;

import java.net.URL;
import java.net.URLConnection;

public class MyHessianProxy extends HessianProxy {

	private static final long serialVersionUID = 3105012772664493848L;
	
	public MyHessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
		super(url, factory, type);
	}

	public MyHessianProxy(URL url, HessianProxyFactory factory) {
		super(url, factory);
	}

	protected void addRequestHeaders(HessianConnection conn) {
		super.addRequestHeaders(conn);
		if (MyHessianProxyFactory.getSessionId() != null) {
			conn.addHeader("Cookie", "JSESSIONID="+MyHessianProxyFactory.getSessionId());
		}
		if (MyHessianProxyFactory.getRememberMe() != null) {
			conn.addHeader("Cookie", "REMEMBER_ME="+MyHessianProxyFactory.getRememberMe());
		}
	}

	protected void parseResponseHeaders(URLConnection conn) {
//		String session = conn.getHeaderField("Set-Cookie");
//		if (session != null) {
//			sessionId = session;
//		}
	}
}
