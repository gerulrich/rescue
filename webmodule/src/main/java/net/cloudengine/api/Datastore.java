package net.cloudengine.api;

import java.io.Serializable;

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
public interface Datastore <E, PK extends Serializable> {

	/**
     * Generic method to get an object based on class and identifier. 
     * An Runtime Exception is thrown if nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     */
    E get(PK id);

	void save(E entity);

	PagingResult<E> list();

	PagingResult<E> list(int page, int size);

	void update(E entity);

	void delete(PK key);

	Query<E> createQuery();

}