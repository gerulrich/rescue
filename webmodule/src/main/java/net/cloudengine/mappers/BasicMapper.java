package net.cloudengine.mappers;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.cloudengine.rpc.model.DataObject;
import net.cloudengine.rpc.model.Value;

import org.apache.commons.beanutils.PropertyUtils;

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
						
		/*
		 * Recorro todas las propiedades del Model.
		 * Para cada propiedad, obtengo el valor de la entidad.
		 * El valor puede ser de alguna de las siguientes clases
		 * 1 - Propiedad comun que está en java.lang (Se setea directamente)
		 * 2 - Si la propiedad es otra Entidad:
		 *     La clase de la entidad al cual tengo que transformar se saca del
		 *     argumento del set del Entidad (del primero).
		 * 3 - Es una coleccion: no soportado todavía.
		 */
		T target = null;
		try {
			// Instancio el XModel
			target = modelClass.newInstance();
			
			BeanInfo beanInfo = Introspector.getBeanInfo(modelClass, Object.class);
			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propdesc) {
				
				String propertyName = pd.getName();
				Object value = null;
				Value annotation = getAnnotation(target, propertyName);
				if (annotation != null) {
					value = readProperty(entity, annotation);
				} else {
					value = readProperty(entity, propertyName);					
				}				
				
				// Es una propiedad simple, la seteo directamente.
				if (value == null || value.getClass().getName().startsWith("java.lang")) {
					
					PropertyUtils.setProperty(target, propertyName, value);
					
				} else {
					
					if (isDataObject(pd.getPropertyType())) {
						Class<?>  otherModelClasss = pd.getPropertyType(); 
						Object otherEntity = value;
						
						Object otherModel = this.fillModel(otherEntity, otherModelClasss);
						PropertyUtils.setProperty(target, propertyName, otherModel);
						
					} else {
						//TODO Es una colección	u otro tipo complejo.
						throw new RuntimeException("Mapeo de Colecciones no implementado");
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return target;
	}
	
	private boolean isDataObject(Class<?> clazz) {
		return clazz.getAnnotation(DataObject.class) != null;
	}
	
	private Value getAnnotation(Object model, String property) {
		try {
			Field field = model.getClass().getDeclaredField(property);
			return field.getAnnotation(Value.class);
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}	    
		
	}
	
	private Object readProperty(Object entity, Value annotation) {
		try {
			
			String field = annotation.value();
			if (field.contains(".")) {
				return readRecursive(entity, field);
			} else {
				return PropertyUtils.getProperty(entity, annotation.value());
			}			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	private Object readRecursive(Object entity, String field) {
		String fields[] = field.split("\\.");
		
		Object tempObject = entity;
		for(String prop : fields) {
			try {
				tempObject = PropertyUtils.getProperty(tempObject, prop);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tempObject;
	}
	
	private Object readProperty(Object entity, String propertyName) {
		try {
			return PropertyUtils.getProperty(entity, propertyName);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
//	public void fillVisible(XModel model, Visible<? extends XModel> target) {
//		/*
//		 * Recorro todas las propiedades del Model.
//		 * Para cada propiedad, obtengo el valor del model.
//		 * El valor puede ser de alguna de las siguientes clases
//		 * 1 - Propiedad comun que está en java.lang (Se setea directamente)
//		 * 2 - Si la propiedad es otro Model:
//		 *     La clase del Visible al cual tengo que transformar se saca del
//		 *     argumento del set del Visible (del primero).
//		 * 3 - Es una coleccion: no soportado todavía.
//		 */
//		try {
//			BeanInfo beanInfo = Introspector.getBeanInfo(model.getClass(), Object.class);
//			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
//			for (PropertyDescriptor pd : propdesc) {
//				
//				String propertyName = pd.getName();
//				Object value = PropertyUtils.getProperty(model, propertyName);
//				
//				
//				/* Resultado del get en el model, hay tres posibilidades para el tipo:
//				 * 1 - Está en el package java.lang
//				 * 2 - Es otro Model
//				 * 3 - Es una colección u otro tipo
//				 */
//				if (value == null || value.getClass().getName().startsWith("java.lang")) {
//					// solo actualizo la propiedad si se modifico.
//					boolean isEquals = compareProperty(model, target, propertyName);
//					if (!isEquals) {
//						PropertyUtils.setProperty(target, propertyName, value);
//					}
//				} else if (value instanceof XModel) {
//					XModel otherModel = (XModel) value;
//					Visible<?> otherVisible = (Visible<?>) PropertyUtils.getProperty(target, propertyName); 
//					
//					
//					
//
//					if (otherVisible != null) {
//						
//						boolean isEquals = compareEntities(otherModel, otherVisible);
//						if (isEquals) {
//							// Ya está seteado el visible. Actualizo las propiedades.
//							this.fillVisible(otherModel, otherVisible);
//						} else {
//							// Se cambio de objeto. Creo uno nuevo con los datos.
//							@SuppressWarnings("unchecked") Class otherVisibleClass = PropertyUtils.getPropertyDescriptor(target, propertyName).getPropertyType();
//							otherVisible = (Visible<?>) otherVisibleClass.newInstance();
//							this.fillVisible(otherModel, otherVisible);
//							PropertyUtils.setProperty(target, propertyName, otherVisible);
//						}
//					} else {
//						// Estaba en null, tengo que crear un nuevo visible.
//						@SuppressWarnings("unchecked") Class otherVisibleClass = PropertyUtils.getPropertyDescriptor(target, propertyName).getPropertyType();
//						otherVisible = (Visible<?>) otherVisibleClass.newInstance();
//						this.fillVisible(otherModel, otherVisible);
//						PropertyUtils.setProperty(target, propertyName, otherVisible);
//					}
//				} else {
//					//TODO Es una colección	u otro tipo complejo
//					throw new RuntimeException("Mapeo de Colecciones no implementado");
//				}
//			}				
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public <T extends Visible<?>> T fillVisible(XModel model, Class<T> vclass) {
//		try {
//			T target = vclass.newInstance();
//			fillVisible(model, target);			
//			return target;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	/**
//	 * Compara los valores de las propiedades entre un Model y un Visible.
//	 * @param model
//	 * @param visible
//	 * @param propertyName
//	 * @return true si las propiedad son iguales, false enc caso contrario.
//	 */
//	protected boolean compareProperty(XModel model, Visible<?> visible, String propertyName) {
//		try {
//			Object modelValue = PropertyUtils.getProperty(model, propertyName);
//			Object visibleValue = PropertyUtils.getProperty(visible, propertyName);
//			return ObjectUtils.equals(modelValue, visibleValue);
//		} catch (IllegalAccessException e) {
//			return false;
//		} catch (InvocationTargetException e) {
//			return false;
//		} catch (NoSuchMethodException e) {
//			return false;
//		}
//	}
//	
//	/**
//	 * Compara un Model y un Visible para determinar si ambos representan la misma entidad. 
//	 * @param model
//	 * @param visible
//	 * @return
//	 */
//	protected boolean compareEntities(XModel model, Visible<?> visible) {
//		
//		// me fijo si el model tiene el campo id
//		boolean tieneCampoId = PropertyUtils.isReadable(model, "id");
//		if (tieneCampoId) {
//			
//			try {
//				Object idModel = PropertyUtils.getProperty(model, "id");
//				Object idVisible = PropertyUtils.getProperty(visible, "id");
//				return ObjectUtils.equals(idModel, idVisible);
//				
//			} catch (IllegalAccessException e) {
//				return false;
//			} catch (InvocationTargetException e) {
//				return false;
//			} catch (NoSuchMethodException e) {
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}
//
}