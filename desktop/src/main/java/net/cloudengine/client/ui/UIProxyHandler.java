package net.cloudengine.client.ui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Display;


public class UIProxyHandler<T> implements InvocationHandler {

	private T object;
	
	public UIProxyHandler(T object) {
		this.object = object;
	}
	
	public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					method.invoke(object, args);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		return null;
	}	
}