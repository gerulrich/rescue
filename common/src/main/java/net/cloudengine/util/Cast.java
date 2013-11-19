package net.cloudengine.util;

import java.util.List;

/**
 * Metodos utilitarios para relizar castings.
 * @author German Ulrich
 *
 */
public class Cast {
	
	public static <T> T as(Class<T> clazz, Object o) {
		if(clazz.isInstance(o)) {
			return clazz.cast(o);
		}
		throw new IllegalArgumentException("Cast invalido: "+o.getClass().getName());
	}
	
	public static <T> boolean isInstance(Class<T> clazz, Object o) {
		return clazz.isInstance(o); 
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> cast(List<?> list, Class<T> clazz) {
		return (List<T>) list;
	}
	
	public static Casteable cast(Object object) {
		return new CasteableImpl(object);
	}
	

}
