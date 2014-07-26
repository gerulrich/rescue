package net.cloudengine.web.crud.support;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CrudUtil {

	private static final String PREFIX_KEY = "crud."; 
	
	public static <T> GridViewModel getGridViewModel(Class<T> clazz) throws Exception {
		GridViewModel result = new GridViewModel();
		result.setTitleKey(PREFIX_KEY + StringUtils.uncapitalize(clazz.getSimpleName())+".list");
		completeGridView(result, clazz, null, null);
		return result;
	}
	
	private static <T> List<String> getFieldListForClass(Class<T> clazz) {
		List<String> fields = new ArrayList<String>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            for(Field field : c.getDeclaredFields()) {
            	if (field.getAnnotation(CrudProperty.class) != null) {
            		fields.add(field.getName());
            	}
            }
        }
        return fields;
	}
	
	private static <T> void completeGridView(GridViewModel gridView, Class<T> clazz, String path, String parent) throws Exception {
		BeanInfo info = Introspector.getBeanInfo(clazz, Object.class);
		List<String> fieldList = getFieldListForClass(clazz);
		for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
			if (!fieldList.contains(pd.getName())) {
				continue;
			}
			
			CrudProperty cp = getAnnotation(clazz, pd.getName());
			if (cp != null && cp.show()) {
				if (cp.embedded()) {
					completeGridView(gridView, pd.getPropertyType(), pd.getName(), StringUtils.uncapitalize(clazz.getSimpleName()));
				} else {
					String key = null;
					if (path == null) {
						if (StringUtils.isBlank(cp.key())) {
							key = PREFIX_KEY + StringUtils.uncapitalize(clazz.getSimpleName())+"."+ pd.getName();
						} else {
							key = PREFIX_KEY + cp.key();
						}
					} else {
						if (StringUtils.isBlank(cp.key())) {
							key = PREFIX_KEY + parent+"."+path+"."+ pd.getName();
						} else {
							key = PREFIX_KEY + "."+path +cp.key();
						}
					}
					String completePath = null;
					if (path != null) {
						completePath = path+"."+pd.getName();
					} else {
						completePath = pd.getName();
					}
					ColumnViewModel column = new ColumnViewModel(key, completePath);
					gridView.addColumn(column);
				}
			}
		}
	}
	
	private static CrudProperty getAnnotation(Class<?> clazz, String property) {
		try {
			Field field = clazz.getDeclaredField(property);
			return field.getAnnotation(CrudProperty.class);
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}

	}

}
