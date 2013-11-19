package net.cloudengine.rpc.mappers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import net.cloudengine.util.UncheckedThrow;

public class ReflectionUtils {
	
	private static final Map<String,Class<?>> builtInMap;
	
	private ReflectionUtils() {		
	}
	
	static {
		builtInMap = new HashMap<String,Class<?>>();
		builtInMap.put("int", Integer.class );
	    builtInMap.put("long", Long.class );
	    builtInMap.put("double", Double.class );
	    builtInMap.put("float", Float.class );
	    builtInMap.put("bool", Boolean.class );
	    builtInMap.put("boolean", Boolean.class );
	    builtInMap.put("char", Character.class );
	    builtInMap.put("byte", Byte.class );
	    builtInMap.put("void", Void.class );
	    builtInMap.put("short", Short.class );
	}
	
	public static void setProperty(Object bean, String name, Object value) {
		try {
			PropertyDescriptor property = findProperty(bean, name);
			if (property == null) {
	        	throw new MappingException(String.format("Property %s of class %s not found", name, bean.getClass().getName()));
	        }
			checkTypes(property.getPropertyType(), value);
			if (!isSameValue(property, bean, value)) {
				property.getWriteMethod().invoke(bean, new Object[] {value});
			}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			UncheckedThrow.throwUnchecked(e);
		}
	}
	
	private static void checkTypes(Class<?> methodArgumentClass, Object value) {
		methodArgumentClass = getWrapperClass(methodArgumentClass);
		if (value == null || methodArgumentClass.isInstance(value)) {
			return;
		}
		if (methodArgumentClass.isAssignableFrom(value.getClass())) {
			return;
		}
		throw new MappingException(String.format("Invalid property type. The property %s of class %s is not of type %s"));
	}
	
	public static Object getProperty(Object bean, String name) {
		Object result = null;
		try {
	        PropertyDescriptor property = findProperty(bean, name);
	        if (property == null) {
	        	throw new MappingException(String.format("property %s not found on class %s", name, bean.getClass().getName()));
	        }
	        result = property.getReadMethod().invoke(bean, new Object[0]);
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			UncheckedThrow.throwUnchecked(e);
		}
		return result;
	}
	
	public static Class<?> getPropertyType(Object bean, String name) {
		Class<?> result = null;
		try {
	        PropertyDescriptor property = findProperty(bean, name);
	        if (property == null) {
	        	throw new MappingException(String.format("property %s not found on class %s", name, bean.getClass().getName()));
	        }
	        result = property.getPropertyType();
		} catch (IntrospectionException e) {
			UncheckedThrow.throwUnchecked(e);
		}
		return result;
	}

	private static PropertyDescriptor findProperty(Object bean, String name) throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor property = null;
		for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
			if (pd.getName().equals(name)) {
				property = pd;
				break;
			}
		}
		return property;
	}
	
	public static Class<?> getWrapperClass(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return builtInMap.get(clazz.getSimpleName()); 
		}
		return clazz;
	}
	
	private static boolean isSameValue(PropertyDescriptor property, Object bean, Object value) {
		try {
			Object tempValue = property.getReadMethod().invoke(bean, new Object[0]);
			return (tempValue != null && tempValue.equals(value));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
	}
	
	public static <T extends Annotation> T getAnnotation(Object model, String property, Class<T> annotationClass) {
		try {
			Field field = model.getClass().getDeclaredField(property);
			return field.getAnnotation(annotationClass);
		} catch (NoSuchFieldException | SecurityException e) {
			UncheckedThrow.throwUnchecked(e);
		}
		return null;
	}
	
	public static Type getFieldType(Object bean, String propertyName) {
		try {
			Field field = bean.getClass().getDeclaredField(propertyName);
			return field.getGenericType();
		} catch (NoSuchFieldException | SecurityException e) {
			UncheckedThrow.throwUnchecked(e);
		}
		return null;
	}
	
	

}
