package net.cloudengine.api.mongo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Id;
import com.mongodb.WriteConcern;

public class MongoStore<E, PK extends Serializable> implements Datastore<E, PK> {

	protected SimpleMongoDbFactory factory;
	protected Class<E> entityClass;
	protected Morphia morphia;
	
	@Autowired
	public MongoStore(MongoDBWrapper wrapper, Class<E> entityClass, Morphia morphia) {
		super();
		this.factory = wrapper.getFactory();
		this.entityClass = entityClass;
		this.morphia = morphia;
	}

	@Override
	public E get(PK id) {
		return createMorphiaDatastore().get(entityClass, id);
	}

	@Override
	public <T> void save(T entity) {
		if (haveID(entity)) {
			throw new IllegalArgumentException("no se puede guardar un objeto con id, para actualizar debe utilizar update");
		}
		createMorphiaDatastore().save(entity);
	}
	
	@Override
	public void delete(PK key) {
		createMorphiaDatastore().delete(entityClass, key);
	}
	
	@Override
	public void delete(Query<E> query) {
		createMorphiaDatastore().delete(((QueryImpl<E,PK>)query).getQuery(), WriteConcern.ACKNOWLEDGED);
	}

	@Override
	public List<E> getAll() {
		return createMorphiaDatastore().find(entityClass).asList();
	}

	@Override
	public PagingResult<E> list() {
		return list(FIRST_PAGE, PAGE_SIZE);
	}

	@Override
	public PagingResult<E> list(int page, int size) {
		return new MongoPagingResult<E>(entityClass, createMorphiaDatastore(), page, size);
	}

	@Override
	public Query<E> createQuery() {
		return new QueryImpl<E,PK>(entityClass, factory.getDb().getMongo(), morphia, factory.getDb().getName());
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
			// TODO handle exception.
		}
		
		return false;
	}

	@Override
	public void update(E entity) {
		if (!haveID(entity)) {
			throw new IllegalArgumentException("no se puede actualizar un objeto sin id, para guardar utilice save");
		}
		createMorphiaDatastore().save(entity);
	}

	@Override
	public void deleteAll() {
		createMorphiaDatastore().getCollection(entityClass).drop();		
	}
	
	protected com.google.code.morphia.Datastore createMorphiaDatastore() {
		return morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName());
	}
}
