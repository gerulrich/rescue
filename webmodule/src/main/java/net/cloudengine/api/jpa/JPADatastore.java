package net.cloudengine.api.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;
import net.cloudengine.util.Cast;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JPADatastore<E, PK extends Serializable> implements Datastore<E, PK> {

	@PersistenceContext(unitName = "webmodule")
	private EntityManager entityManager;
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
	public Class<E> getType() {
		return this.persistentClass;
	}

	@Override
	public E get(PK id) {
		return this.entityManager.find(persistentClass, id);
	}

	@Override
	public <T> void save(T entity) {
		 this.entityManager.persist(entity);
	}
	
	@Override
	public List<E> getAll() {
		Session session = getHibernateSession();
		Criteria criteria = session.createCriteria(persistentClass); 
		return cast(criteria.list());
	}

	@Override
	public PagingResult<E> list() {
		return list(FIRST_PAGE,PAGE_SIZE);
	}

	@Override
	public PagingResult<E> list(int page, int size) {
		return new JPAPagingResult<E>(persistentClass, entityManager, page, size);
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
	public void delete(Query<E> query) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Query<E> createQuery() {
		Session session = this.getHibernateSession();
		Criteria criteria = session.createCriteria(persistentClass);
		return new QueryJpaImpl<E, PK>(criteria, this);
	}

	@Override
	public void deleteAll() {
		String hqlQuery = "delete from "+persistentClass.getSimpleName(); 
		this.entityManager.createQuery(hqlQuery).executeUpdate();
	}
	
	protected <T> List<T> cast(List<?> list, Class<T> clazz) {
		return Cast.cast(list, clazz);
	}
	
	protected List<E> cast(List<?> list) {
		return Cast.cast(list, persistentClass);
	}
	
	protected E cast(Object object) {
		return Cast.as(persistentClass, object);
	}

	public Class<E> getPersistentClass() {
		return persistentClass;
	}
	
	protected EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	@Deprecated
	public E findOne(String field, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public List<E> findAll(String field, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
