package net.cloudengine.client.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.IProgressMonitor;


public class MethodExecutor<T> implements Runnable {

	private Object proxy;
	private Method method;
	private Object[] args;
	private Callback callback;
	private IProgressMonitor monitor;
	
	public MethodExecutor(Object proxy, Method method, Object[] args, Callback callback, IProgressMonitor monitor) {
		super();
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.callback = callback;
		this.monitor = monitor;
	}
	
	public MethodExecutor(Object proxy, Method method, Object[] args, Callback callback) {
		super();
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.callback = callback;
	}
	
	@Override
	public void run() {
		
		try {
			if (monitor != null) {
				JobUtils.uiExec(monitor).beginTask("Por favor espere...", 0);
			}
			Object obj = method.invoke(proxy, args);
			callback.doAction(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			if (monitor != null) {
				JobUtils.uiExec(monitor).done();
			}
		}
		
	}

}
