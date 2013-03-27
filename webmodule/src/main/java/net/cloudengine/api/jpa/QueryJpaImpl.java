package net.cloudengine.api.jpa;

import java.io.Serializable;
import java.util.Collection;

import net.cloudengine.api.Field;
import net.cloudengine.api.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class QueryJpaImpl<E,PK extends Serializable> implements Query<E>, Field<E> {

	private JPADatastore<E, PK> datastore;
	private Criteria criteria;
	private String fieldName;
	
	public QueryJpaImpl(Criteria criteria, JPADatastore<E, PK> datastore) {
		this.criteria = criteria;
		this.datastore = datastore;
	}
	
	@Override
	public Field<E> field(String name) {
		this.fieldName = name;
		return this;
	}

	@Override
	public E get() {
		criteria.setMaxResults(1);
		return datastore.cast(criteria.uniqueResult());
	}

	@Override
	public Collection<E> list() {
		return Cast.cast(criteria.list(), datastore.getPersistentClass());
	}
	
	@Override
	public Query<E> eq(Object object) {
		criteria.add(Restrictions.eq(fieldName, object));
		return this;
	}

	@Override
	public Query<E> ge(Object object) {
		criteria.add(Restrictions.ge(fieldName, object));
		return this;
	}

	@Override
	public Query<E> gt(Object object) {
		criteria.add(Restrictions.gt(fieldName, object));
		return this;
	}

	@Override
	public Query<E> le(Object object) {
		criteria.add(Restrictions.le(fieldName, object));
		return this;
	}

	@Override
	public Query<E> lt(Object object) {
		criteria.add(Restrictions.lt(fieldName, object));
		return this;
	}

}
