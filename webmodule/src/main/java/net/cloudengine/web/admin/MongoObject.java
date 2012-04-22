package net.cloudengine.web.admin;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MongoObject {
	
	private Set<String> propertiesNames;
	
	private Map<String,Object> values;
	
	public Object getValue(String name) {
		return values.get(name);
	}

	public void setValues(Map<String, Object> values) {
		this.values = filtrar(values);
		this.propertiesNames = this.values.keySet();
	}

	public Set<String> getPropertiesNames() {
		return propertiesNames;
	}
	
	public Map<String, Object> getValues() {
		return values;
	}

	private Map<String,Object> filtrar(Map<String, Object> values) {
		Map<String,Object> filtrados = new LinkedHashMap<String, Object>();
		for(String key : values.keySet()) {
			Object val = values.get(key);
			if (key.equals("password") || key.equals("className")) {
				continue;
			}
			
			if (!isComplex(val)) {
				filtrados.put(key, val);
			} else if (!val.getClass().isArray()) {
				filtrados.put(key, val.toString());
			}
		}
		return filtrados;
	}
	
	private boolean isComplex(Object object) {
		if (object.getClass().getName().startsWith("com.mongo") ||
			object.getClass().equals(Boolean.class) || 
			object.getClass().isArray() ||
			!object.getClass().getName().startsWith("java.lang")
			) {
			return true;
		} else {
			return false;
		}
	}

}