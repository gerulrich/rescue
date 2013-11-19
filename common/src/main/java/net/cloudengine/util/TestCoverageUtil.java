package net.cloudengine.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestCoverageUtil {
	
	public static Object reflectiveInvokePrivateConstructor(Class<?> clazz) {
		Object n = null;
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructors()[0]; 
			constructor.setAccessible(true); 
			n = constructor.newInstance((Object[])null);
		} catch (Exception e) {
			UncheckedThrow.throwUnchecked(e);
		}
		return n;
	}
	
	public static void reflectiveInvokeHiddenBridgeMethod(Object instance, String methodName, Class<?> argTypes[], Object...arguments) {
		try {
			Method method = instance.getClass().getMethod(methodName, argTypes);
			method.invoke(instance, arguments);
		} catch (Exception e) {
			UncheckedThrow.throwUnchecked(e);
		}
	}

}
