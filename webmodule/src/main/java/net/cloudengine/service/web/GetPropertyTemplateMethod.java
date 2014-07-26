package net.cloudengine.service.web;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Date;
import java.util.List;

import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleDate;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * {@link TemplateMethodModelEx} que permite obtener una propiedad de un objeto
 * pasando el nombre de la misma.
 * @author German Ulrich
 *
 */
public class GetPropertyTemplateMethod implements TemplateMethodModelEx {

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List args) throws TemplateModelException {
		if (args.size() != 2) {
            throw new TemplateModelException("Wrong number of arguments");
        }
		Object obj = ((StringModel) args.get(0)).getWrappedObject();
		Object method = args.get(1);
		if (!(method instanceof SimpleScalar)) {
			throw new TemplateModelException("Wrong arguments");
		}
		String path[] = ((SimpleScalar)method).getAsString().split("\\.");
		for(String property : path) {
			obj = getProperty(obj, property);
			if (obj == null) {
				break;
			}
		}
		if (obj == null || obj instanceof String) {
			obj = new SimpleScalar(obj != null ? (String)obj : "");
		} else if (obj instanceof Number) {
			obj = new SimpleNumber((Number)obj);
		} else if (obj instanceof Date) {
			obj = new SimpleDate((Date)obj);
		} else {
			obj = new SimpleScalar(obj.toString());
		}
		return obj;
	}
	
	private Object getProperty(Object object, String propertyName) throws TemplateModelException {
		try {
			BeanInfo info = Introspector.getBeanInfo(object.getClass(), Object.class);
			for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if (propertyName.equals(pd.getName())) {
					return pd.getReadMethod().invoke(object, new Object[0]);
				}
			}
		} catch (Exception e) {
			 throw new TemplateModelException(e);
		}
		throw new TemplateModelException("property "+propertyName+" not found in class "+object.getClass());
	}
}