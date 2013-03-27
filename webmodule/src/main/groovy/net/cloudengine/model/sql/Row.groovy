package net.cloudengine.model.sql

import java.util.HashMap;
import java.util.Map;

class Row {

//	Object[] tuple; 
	String[] aliases;
	
	Map<String, Object> values;
	
	public Row(Object[] tuple, String[] aliases) {
		super();
//		this.tuple = tuple;
		this.aliases = aliases;
		this.values = new HashMap<String, Object>(tuple.length);

		for (int i = 0; i < tuple.length; i++) {
            String alias = aliases[i];
            if (alias != null) {
            	Object obj = tuple[i];
            	if (!obj.getClass().isArray()) {
            		values.put(alias, obj);
            	} else {
            		values.put(alias, "-");
            	}
            }
		}
	}
}
