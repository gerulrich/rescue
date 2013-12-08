package net.cloudengine.api.mongo;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;
import net.cloudengine.util.UncheckedThrow;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;


public class MongoStore<E, PK extends Serializable> implements Datastore<E, PK> {

	protected Class<E> entityClass;
	protected MongoTemplate mongoTemplate;
	
	@Autowired
	public MongoStore(MongoTemplate mongoTemplate, Class<E> entityClass) {
		super();
		this.entityClass = entityClass;
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public Class<E> getType() {
		return this.entityClass;
	}

	@Override
	public E get(PK id) {
		return mongoTemplate.findById(id, entityClass);
	}

	@Override
	public <T> void save(T entity) {
		if (haveID(entity)) {
			throw new IllegalArgumentException("no se puede guardar un objeto con id, para actualizar debe utilizar update");
		}
		mongoTemplate.save(entity);
	}
	
	@Override
	public void delete(PK key) {
		mongoTemplate.remove(get(key));
	}
	
	@Override
	public void delete(Query<E> query) {
//		createMorphiaDatastore().delete(((QueryImpl<E,PK>)query).getQuery(), WriteConcern.ACKNOWLEDGED);
	}

	@Override
	public List<E> getAll() {
		return mongoTemplate.findAll(entityClass);
	}

	@Override
	public PagingResult<E> list() {
		return list(FIRST_PAGE, PAGE_SIZE);
	}

	@Override
	public PagingResult<E> list(int page, int size) {
		return new MongoPagingResult<E>(mongoTemplate, entityClass, page, size);
	}

	@Override
	public Query<E> createQuery() {
//		return new QueryImpl<E,PK>(entityClass, factory.getDb().getMongo(), morphia, factory.getDb().getName());
		return null;
	}
	
	@Override
	@Deprecated
	public E findOne(String field, Object value) {
		return mongoTemplate.findOne(query(where(field).is(value)), entityClass);
	}
	
	@Override
	@Deprecated
	public List<E> findAll(String field, Object value) {
		return mongoTemplate.find(query(where(field).is(value)), entityClass);
	}

	protected <T> boolean haveID(T entity) {
		try {
			Field rectField[]  = entity.getClass().getDeclaredFields();
			for( Field field : rectField) {
				Id annotationId = field.getAnnotation(Id.class);
				if (annotationId != null) {
					Object id = PropertyUtils.getProperty(entity, field.getName());
					return id != null;
				}
			}
		} catch (Exception e) {
			UncheckedThrow.throwUnchecked(e);
		}
		
		return false;
	}

	@Override
	public void update(E entity) {
		if (!haveID(entity)) {
			throw new IllegalArgumentException("no se puede actualizar un objeto sin id, para guardar utilice save");
		}
//		createMorphiaDatastore().save(entity);
	}

	@Override
	public void deleteAll() {
//		createMorphiaDatastore().getCollection(entityClass).drop();		
	}
	
//	protected com.google.code.morphia.Datastore createMorphiaDatastore() {
//		return morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName());
//	}
}
