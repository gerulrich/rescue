package net.cloudengine.api.mongo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;
import net.cloudengine.web.MongoDBWrapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Id;
import com.mongodb.Mongo;

public class MongoStore<E, PK extends Serializable> implements Datastore<E, PK> {

	private SimpleMongoDbFactory factory;
	private Class<E> entityClass;
	private Morphia morphia;
	
	@Autowired
	public MongoStore(MongoDBWrapper wrapper, Class<E> entityClass, Morphia morphia) {
		super();
		this.factory = wrapper.getFactory();
		this.entityClass = entityClass;
		this.morphia = morphia;
	}

	@Override
	public E get(PK id) {
		return morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).get(entityClass, id);
	}

	@Override
	public void save(E entity) {
		if (haveID(entity)) {
			throw new IllegalArgumentException("no se puede guardar un objeto con id, para actualizar debe utilizar update");
		}
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).save(entity);
	}
	
	@Override
	public void delete(PK key) {
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).delete(entityClass, key);
	}
	
	

	@Override
	public List<E> getAll() {
		Mongo mongo = factory.getDb().getMongo();
		String dbName = factory.getDb().getName();
		return morphia.createDatastore(mongo, dbName)
				.find(entityClass).asList();
	}

	@Override
	public PagingResult<E> list() {
		return list(FIRST_PAGE, PAGE_SIZE);
	}

	@Override
	public PagingResult<E> list(int page, int size) {
		return new MongoPagingResult<E>(entityClass, morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()), page, size);
	}

	@Override
	public Query<E> createQuery() {
		return new QueryImpl<E,PK>(entityClass, factory.getDb().getMongo(), morphia, factory.getDb().getName());
	}
	
	protected boolean haveID(E entity) {
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
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).save(entity);
	}

	@Override
	public void deleteAll() {
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).
			getCollection(entityClass).drop();		
	}
}
