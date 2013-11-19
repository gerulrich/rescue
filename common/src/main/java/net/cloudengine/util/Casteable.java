package net.cloudengine.util;

import java.util.List;

public interface Casteable {
	
	<T> List<T> asList(Class<T> clazz);
	
	Integer asInt();
	
	Long asLong();
	
	String asString();

}
