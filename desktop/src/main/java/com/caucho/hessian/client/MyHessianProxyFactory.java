package com.caucho.hessian.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;

import com.caucho.hessian.io.HessianRemoteObject;


public class MyHessianProxyFactory extends HessianProxyFactory {
	
	private static String sessionId;
	private static String rememberMe;
	
	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		MyHessianProxyFactory.sessionId = sessionId;
	}
	
	public static String getRememberMe() {
		return rememberMe;
	}

	public static void setRememberMe(String rememberMe) {
		MyHessianProxyFactory.rememberMe = rememberMe;
	}

	public Object create(Class<?> api, URL url, ClassLoader loader) {
		if (api == null)
			throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
	    
		InvocationHandler handler = null;

		handler = new MyHessianProxy(url, this, api);

	    return Proxy.newProxyInstance(loader, new Class[] { api, HessianRemoteObject.class }, handler);
	}

}
