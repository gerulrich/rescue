package net.cloudengine.mapviewer.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotationUtil {
	
	public static <T extends Annotation> boolean hasFieldAnnotation(Object object, Class<T> annotationClass) {
		boolean findAnnotation = false;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass(), Object.class);
			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propdesc) {
				String name = pd.getName();
				T annotation = getAnnotation(object, annotationClass, name);
				if (annotation != null) {
					findAnnotation = true;
					break;
				}
			}
		} catch (Exception e) {
			//FIXME
			return false;
		}
		return findAnnotation;
	}
	
	private static <T extends Annotation> T getAnnotation(Object object, Class<T> annotationClass, String property) {
		try {
			Field field = object.getClass().getDeclaredField(property);
			return field.getAnnotation(annotationClass);
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}	    
		
	}

}
