package net.cloudengine.api;

public interface Field<E> {

	Query<E> eq(Object object);

	Query<E> ge(Object object);

	Query<E> gt(Object object);

	Query<E> le(Object object);

	Query<E> lt(Object object);

}
