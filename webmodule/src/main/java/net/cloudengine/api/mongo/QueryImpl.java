package net.cloudengine.api.mongo;

import net.cloudengine.api.Field;
import net.cloudengine.api.Query;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.FieldEnd;
import com.mongodb.Mongo;

public class QueryImpl<E, PK> implements Query<E>, Field<E> {

	private com.google.code.morphia.query.Query<E> query;
	private FieldEnd<? extends com.google.code.morphia.query.Query<E>> fieldEnd;
	
	public QueryImpl(Class<E> entityClass, Mongo mongo, Morphia morphia, String dbName) {
		super();
		this.query = morphia.createDatastore(mongo, dbName).createQuery(entityClass);
	}

	@Override
	public Field<E> field(String name) {
		fieldEnd = query.field(name);
		return this;
	}

	@Override
	public E get() {
		return query.get();
	}

	@Override
	public Query<E> eq(Object object) {
		this.query = fieldEnd.equal(object);
		return this;
	}

	@Override
	public Query<E> ge(Object object) {
		this.query = fieldEnd.greaterThanOrEq(object);
		return this;
	}

	@Override
	public Query<E> gt(Object object) {
		this.query = fieldEnd.greaterThan(object);
		return this;
	}

	@Override
	public Query<E> le(Object object) {
		this.query = fieldEnd.lessThanOrEq(object);
		return this;
	}

	@Override
	public Query<E> lt(Object object) {
		this.query = fieldEnd.lessThan(object);
		return this;
	}
}