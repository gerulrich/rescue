package net.cloudengine.mapviewer.tools.selection;

import java.util.List;

public class Cast {
	
	private Cast() {
        //clase utilitaria, no se instancia nunca.
    }
	
	public static <T> T as(Class<T> clazz, Object o) {
		if(clazz.isInstance(o)) {
			return clazz.cast(o);
		}
		throw new IllegalArgumentException("Cast invalido: "+o.getClass().getName());
	}	
	
	public static <T> boolean isInstance(Class<T> clazz, Object o) {
		return clazz.isInstance(o); 
	}	
	
	public static <T> boolean isInstance(Object o, Class<T> clazz) {
		return clazz.isInstance(o); 
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> cast(List<?> list, Class<T> clazz) {
		return (List<T>) list;
	}

}
