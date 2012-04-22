package net.cloudengine.client.ui;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class AnnotatedCallbackResolver implements Callback {

	private Object formObject;
	private String name;
	
	public AnnotatedCallbackResolver(Object formObject, String name) {
		this.formObject = formObject;
		this.name = name;
	}
	
	@Override
	public void doAction(Object result) {
		try {
			BeanInfo bi = Introspector.getBeanInfo(formObject.getClass(), Object.class);
			for(MethodDescriptor md : bi.getMethodDescriptors()) {
				PostCallback annotation = md.getMethod().getAnnotation(PostCallback.class);
				if (annotation != null && annotation.name() != null && annotation.name().equals(name)) {
					executeMethod(formObject, md.getMethod(), result);
				}
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onError() {
		
	}
	
	
	private void executeMethod(Object object, Method method, Object args) {
		try {
			method.invoke(object, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
