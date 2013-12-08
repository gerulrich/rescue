package net.cloudengine.dao.support;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO (Data Access Object) with common methods to CRUD POJOs.
 *
 * <p>Extend this interface if you want typesafe (no casting necessary) DAO's for your
 * domain objects.
 *
 * @author German Ulrich
 * @param <E> a entity type
 * @param <PK> the primary key for that entity
 */
public interface Repository<E, PK extends Serializable> {

	public static final int FIRST_PAGE = 1;
	public static final int PAGE_SIZE = 10;
	
    E get(PK id);

	<T> void save(T entity);

	List<E> getAll();
	
	Page<E> list();

	Page<E> list(int page, int size);

	<T> void update(T entity);

	void delete(PK key);
	
	Class<E> getType();

	void deleteAll();
	
	Page<E> find(SearchParameters searchParameters, int page, int size);
	
	Page<E> list(SearchParameters searchParameters);
	
	E findOne(SearchParameters searchParameters);
	

	
}
