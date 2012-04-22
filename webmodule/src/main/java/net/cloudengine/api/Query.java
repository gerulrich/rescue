package net.cloudengine.api;

import java.util.Collection;

public interface Query<E> {

	Field<E> field(String name);
	
	E get();
	
	Collection<E> list();
	
}
