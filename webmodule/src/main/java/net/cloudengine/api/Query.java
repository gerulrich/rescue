package net.cloudengine.api;

public interface Query<E> {

	Field<E> field(String name);
	
	E get();
	
}
