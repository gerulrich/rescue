package net.cloudengine.model.console

import com.mongodb.BasicDBList

class MongoObject {
	
	private Map<String,Object> values;
	
	public Object getValue(String name) {
		return values.get(name);
	}

	void setValues(Map<String, Object> values) {
		this.values = filtrar(values);
	}

	Map<String, Object> getValues() {
		return values;
	}

	Map<String,Object> filtrar(Map<String, Object> values) {
		Map<String,Object> filtrados = new LinkedHashMap<String, Object>();
		for(String key : values.keySet()) {
			Object val = values.get(key);
			if (!isComplex(val)) {
				filtrados.put(key, val);
			} else if (!val.getClass().isArray() && !(val instanceof BasicDBList)) {
				filtrados.put(key, val.toString());
			} else if (val instanceof BasicDBList) {
				filtrados.put(key, "-");
			}
		}
		return filtrados;
	}
	
	boolean isComplex(Object object) {
		return (object.getClass().getName().startsWith("com.mongo") ||
			object.getClass().equals(Boolean.class) ||
			object.getClass().isArray() ||
			!object.getClass().getName().startsWith("java.lang")
		);
	}

}
