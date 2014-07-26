package net.cloudengine.dao.jpa.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.cloudengine.dao.support.Page;
import net.cloudengine.dao.support.PageCollection;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.SearchParameters;
import net.cloudengine.util.Cast;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JPARepository<E,PK extends Serializable> implements Repository<E, PK> {

	private static final int ONE_RESULT = 1;
	@PersistenceContext(unitName = "webmodule")
	private EntityManager entityManager;
	private Class<E> persistentClass;
	
	public JPARepository(final Class<E> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public JPARepository(final Class<E> persistentClass, EntityManager entityManager) {
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
	public Page<E> list() {
		return list(FIRST_PAGE, PAGE_SIZE);
	}

	@Override
	public Page<E> list(int page, int size) {
		return new JPAPage<E>(this.persistentClass, this.entityManager, page, size);
	}

	@Override
	public <T> void update(T entity) {
		this.entityManager.merge(entity);		
	}

	@Override
	public void delete(PK key) {
		this.entityManager.remove(this.get(key));		
	}

	@Override
	public Class<E> getType() {
		return this.persistentClass;
	}

	@Override
	public void deleteAll() {
		String hqlQuery = "delete from "+persistentClass.getSimpleName(); 
		this.entityManager.createQuery(hqlQuery).executeUpdate();		
	}

	@Override
	public Page<E> find(SearchParameters searchParameters, int page, int size) {
		Criteria criteriaCount = JPAUtils.createQuery(searchParameters, getHibernateSession(), getType());
		criteriaCount.setProjection(Projections.rowCount());
		Long totalSize = (Long) criteriaCount.uniqueResult(); 
		
		Criteria criteria = JPAUtils.createQuery(searchParameters, getHibernateSession(), getType());
		criteria.setFirstResult((page-1)*page);
		criteria.setMaxResults(size);
		List<E> list = cast(criteria.list());
		
		return new PageCollection<E>(list, page, size, totalSize);
	}

	@Override
	public List<E> findAll(SearchParameters searchParameters) {
		Criteria criteria = JPAUtils.createQuery(searchParameters, getHibernateSession(), getType());
		return cast(criteria.list());
	}

	@Override
	public Page<E> list(SearchParameters searchParameters) {
		return find(searchParameters, FIRST_PAGE, PAGE_SIZE);
	}

	@Override
	public E findOne(SearchParameters searchParameters) {
		Criteria criteria = JPAUtils.createQuery(searchParameters, getHibernateSession(), getType());
		criteria.setMaxResults(ONE_RESULT);
		return cast(criteria.uniqueResult());
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

	protected EntityManager getEntityManager() {
		return this.entityManager;
	}
	
}
