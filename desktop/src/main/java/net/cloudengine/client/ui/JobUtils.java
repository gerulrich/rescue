package net.cloudengine.client.ui;

import java.lang.reflect.Proxy;

import org.eclipse.core.runtime.IProgressMonitor;


public class JobUtils {

	@SuppressWarnings("unchecked")
	public static <T> T execAsync(T controller, Callback callback) {
		System.out.println("Creando proxy");
		AsyncProxyHandler<T> handler = new AsyncProxyHandler<T>(controller, callback);
		T proxyController = (T) Proxy.newProxyInstance(controller.getClass().getClassLoader(),  controller.getClass().getInterfaces(),  handler); 
		return proxyController;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T execAsync(T controller, Callback callback, IProgressMonitor monitor) {
		System.out.println("Creando proxy");
		AsyncProxyHandler<T> handler = new AsyncProxyHandler<T>(controller, callback, monitor);
		T proxyController = (T) Proxy.newProxyInstance(controller.getClass().getClassLoader(),  controller.getClass().getInterfaces(),  handler); 
		return proxyController;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T uiExec(T object) {
		UIProxyHandler<T> handler = new UIProxyHandler<T>(object);
		T proxyObject = (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),  object.getClass().getInterfaces(),  handler); 
		return proxyObject;
	}
}
