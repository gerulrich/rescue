package net.cloudengine.api.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;

import org.hibernate.Session;

public class JPADatastore<E, PK extends Serializable> implements
		Datastore<E, PK> {

	@PersistenceContext(unitName = "webmodule")
	protected EntityManager entityManager;
	private Class<E> persistentClass;

	public JPADatastore(final Class<E> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public JPADatastore(final Class<E> persistentClass, EntityManager entityManager) {
		this.persistentClass = persistentClass;
		this.entityManager = entityManager;
	}

	/**
	 * Retorna la sesion de hibernate por si se quiere utilizar las criterias de
	 * hibernate en lugar de JPA.
	 * 
	 * @return sesion de hibernate
	 */
	protected Session getHibernateSession() {
		return (Session) this.entityManager.getDelegate();
	}

	@Override
	public E get(PK id) {
		return this.entityManager.find(persistentClass, id);
	}

	@Override
	public void save(E entity) {
		 this.entityManager.persist(entity);
	}

	@Override
	public PagingResult<E> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagingResult<E> list(int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(E entity) {
		this.entityManager.merge(entity);
	}

	@Override
	public void delete(PK key) {
		 this.entityManager.remove(this.get(key));
	}

	@Override
	public Query<E> createQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
	}

}
