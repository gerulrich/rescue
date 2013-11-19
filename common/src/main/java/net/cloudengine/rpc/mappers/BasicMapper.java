package net.cloudengine.rpc.mappers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.cloudengine.util.UncheckedThrow;


public class BasicMapper implements DTOMapper {

	public <T> List<T> fillModels(Collection<?> entities, Class<T> modelClass) {
		List<T> result = new ArrayList<T>();
		if (entities != null) {
			for(Object entity : entities) {
				T model = this.fillModel(entity, modelClass);
				result.add(model);
			}
		}
		return result;
	}

	public <T> T fillModel(Object entity, Class<T> modelClass) {
		
		if (!isDataObject(modelClass)) {
			throw new RuntimeException("La clase "+modelClass+" no es un DataObject");
		}
						
		T target = null;
		try {
			// Instancio el XModel
			target = modelClass.newInstance();
			
			BeanInfo beanInfo = Introspector.getBeanInfo(modelClass, Object.class);
			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propdesc) {
				processPropertyFillModel(pd, entity, target);
			}
		} catch (IntrospectionException | InstantiationException | IllegalAccessException e) {
			UncheckedThrow.throwUnchecked(e);
		}
		return target;
	}

	private <T> void processPropertyFillModel(PropertyDescriptor pd, Object entity, T target) {
		String propertyName = pd.getName();
		Object value = null;
		Value annotation = ReflectionUtils.getAnnotation(target, propertyName, Value.class);
		ValueTransformer transformer = getTransformer(annotation);
		if (annotation != null) {
			value = readProperty(entity, annotation);
		} else {
			value = readProperty(entity, propertyName);					
		}
		
		// Es una propiedad simple, la seteo directamente.
		if (value == null || isJavaSimpleType(pd.getPropertyType())) {
			ReflectionUtils.setProperty(target, propertyName, transformer.transform(value));
		} else {
			
			if (isDataObject(pd.getPropertyType())) {
				Class<?>  otherModelClasss = pd.getPropertyType(); 
				Object otherEntity = value;
				
				Object otherModel = this.fillModel(otherEntity, otherModelClasss);
				ReflectionUtils.setProperty(target, propertyName, otherModel);
				
			} else {
				ListValue listValue = ReflectionUtils.getAnnotation(target, propertyName, ListValue.class);
				if (listValue!= null && isParameterizedType(target, propertyName)) {
					List<?> originalValues = (List<?>)value;
					List<Object> listValues = new ArrayList<>();
			        Type type = ReflectionUtils.getFieldType(target, propertyName);
			        ParameterizedType pt = (ParameterizedType) type;
			        Type clazzType = pt.getActualTypeArguments()[0];
			        for(Object object : originalValues) {
			        	Object model = fillModel(object, (Class<?>) clazzType);
			        	listValues.add(model);
			        }
			        ReflectionUtils.setProperty(target, propertyName, listValues);
				} else {
					//TODO Es una colección	u otro tipo complejo.
					throw new RuntimeException("Mapeo de Colecciones no implementado");
				}				
			}
		}
	}
	
	private boolean isParameterizedType(Object bean, String propertyName) {
		Type type = ReflectionUtils.getFieldType(bean, propertyName);
		return type instanceof ParameterizedType;
	}
	
	private boolean isDataObject(Class<?> clazz) {
		return clazz.getAnnotation(DataObject.class) != null;
	}
	
	private ValueTransformer getTransformer(Value value) {
		ValueTransformer result = null;
		if (value != null) {
			Class<? extends ValueTransformer> clazz = value.transformer();
			try {
				result = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				UncheckedThrow.throwUnchecked(e);
			}
		} else {
			result = new IdentityTransformer();
		}
		return result;
	}
	
	private Object readProperty(Object entity, Value annotation) {
		String field = annotation.value();
		if (field.contains(".")) {
			return readRecursive(entity, field);
		} else {
			return ReflectionUtils.getProperty(entity, annotation.value());
		}
	}
	
	private void setProperty(Object entity, Value annotation, Object value) {
		String field = annotation.value();
		if (field.contains(".")) {
			setRecursive(entity, field, value);
		} else {
			ReflectionUtils.setProperty(entity, annotation.value(), value);
		}			
	}
	
	private void setProperty(Object entity, String property, Object value) {
		if (property.contains(".")) {
			setRecursive(entity, property, value);
		} else {
			ReflectionUtils.setProperty(entity, property, value);
		}			
	}
	
	private void setRecursive(Object entity, String field, Object value) {
		String fields[] = field.split("\\.");
		Object tempObject = entity;
		
		for(int i=0; i < fields.length-1;i++) {
			Object propertyObject = ReflectionUtils.getProperty(tempObject, fields[i]);
			if (propertyObject != null) {
				tempObject = propertyObject; 
			} else {
				Class<?> type = ReflectionUtils.getPropertyType(tempObject, fields[i]);
				try {
					Object typeInstance = type.newInstance();
					ReflectionUtils.setProperty(tempObject, fields[i], typeInstance);
					tempObject = typeInstance;
				} catch (InstantiationException | IllegalAccessException e) {
					UncheckedThrow.throwUnchecked(e);
				}
			}
		}
		ReflectionUtils.setProperty(tempObject, fields[fields.length-1], value);
		
		return;
	}	
	
	private Object readRecursive(Object entity, String field) {
		String fields[] = field.split("\\.");
		
		Object tempObject = entity;
		for(String prop : fields) {
			tempObject = ReflectionUtils.getProperty(tempObject, prop);
		}
		return tempObject;
	}
	
	private Object readProperty(Object entity, String propertyName) {
		return ReflectionUtils.getProperty(entity, propertyName);
	}	
	
	@Override
	public <T> void fillEntity(Object model, T entity) {

		if (!isDataObject(model.getClass())) {
			throw new RuntimeException("La clase "+model.getClass()+" no es un DataObject");
		}

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(model.getClass(), Object.class);
			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propdesc) {
				if (!isReadOnly(model, pd.getName())) {
					processPropertyFillEntity(pd, model, entity);
				}
			}
		} catch (IntrospectionException e) {
			UncheckedThrow.throwUnchecked(e);
		}
	}
	
	private boolean isReadOnly(Object bean, String property) {
		return ReflectionUtils.getAnnotation(bean, property, ReadOnly.class) != null;
	}

	@Override
	public <T> T fillEntity(Object model, Class<T> entityClass) {
		
		if (!isDataObject(model.getClass())) {
			throw new RuntimeException("La clase "+model.getClass()+" no es un DataObject");
		}
		
		T target = null;
		try {
			// Instancio la entidad
			target = entityClass.newInstance();
			fillEntity(model, target);
		} catch (Exception e) {
			UncheckedThrow.throwUnchecked(e);
		}
		
		return target;
	}	
	
	private <T> void processPropertyFillEntity(PropertyDescriptor property, Object model, T entity) {
		String propertyName = property.getName();
		Object value = null;
		Value annotation = ReflectionUtils.getAnnotation(model, propertyName, Value.class);
		ValueTransformer transformer = getTransformer(annotation);
		value = ReflectionUtils.getProperty(model, propertyName);
		
		if (value != null && isDataObject(value.getClass())) {
			if (annotation != null) {
				
			} else {
				Object se = ReflectionUtils.getProperty(entity, propertyName);
				if (se != null && isSameEntity(se, value)) {
					fillEntity(value, se);
				} else {
					Class<?> entitySubClass = ReflectionUtils.getPropertyType(entity, propertyName); 
					se = fillEntity(value, entitySubClass);
					ReflectionUtils.setProperty(entity, propertyName, se);
				}
			}
		} else if (value == null || isJavaSimpleType(value.getClass())) {
			// Es una propiedad simple o null, la seteo directamente.

			if (annotation != null) {
				this.setProperty(entity, annotation, transformer.inverse(value));
			} else {
				this.setProperty(entity, propertyName, transformer.inverse(value));
			}
		}
	}
	
	private boolean isJavaSimpleType(Class<?> clazz) {
		if (Collection.class.isAssignableFrom(clazz)) {
			return false;
		}
		if (clazz.isArray()) {
			clazz = clazz.getComponentType();
		}
		clazz = ReflectionUtils.getWrapperClass(clazz);
		return clazz.getName().startsWith("java.lang") || clazz.getName().startsWith("java.util"); 
	}
	
	private boolean isSameEntity(Object entity, Object value) {
		String propertyIdName = findIdProperty(value);
		if (propertyIdName != null) {
			Value annotation = ReflectionUtils.getAnnotation(value, propertyIdName, Value.class);
			Object oldId = ReflectionUtils.getProperty(entity, annotation != null ? annotation.value():propertyIdName);
			Object newId = ReflectionUtils.getProperty(value, propertyIdName);
			return (oldId != null && oldId.equals(newId));
		}
		return true;
	}
	
	private String findIdProperty(Object entity) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass(), Object.class);
			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propdesc) {
				if (ReflectionUtils.getAnnotation(entity, pd.getName(), Id.class) != null) {
					return pd.getName();
				}
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}