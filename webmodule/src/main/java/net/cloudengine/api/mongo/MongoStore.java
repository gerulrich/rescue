package net.cloudengine.api.mongo;

import java.io.Serializable;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.Query;
import net.cloudengine.web.MongoDBWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.google.code.morphia.Morphia;

public class MongoStore<E, PK extends Serializable> implements Datastore<E, PK> {

	private static final int FIRST_PAGE = 1;
	private static final int PAGE_SIZE = 10;
	
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
        /*Field rectField[]  = entity.getClass().getDeclaredFields();
        for( Field field : rectField) {
        	Id annotationId = field.getAnnotation(Id.class);
        	if (annotationId != null) {
        		String name = field.getName();
        		
        		String methodName = "get"+StringUtils.capitalize(name);
        		try {
					Object obj = entity.getClass().getMethod(methodName, new Class[0]);
					if (obj != null) {
						throw new IllegalArgumentException("no se puede guardar un objeto con id, debe utilizar update");
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }*/
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).save(entity);
	}
	
	@Override
	public void delete(PK key) {
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).delete(entityClass, key);
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

	@Override
	public void update(E entity) {
		/*Field rectField[]  = entity.getClass().getDeclaredFields();
        for( Field field : rectField) {
        	Id annotationId = field.getAnnotation(Id.class);
        	if (annotationId != null) {
        		String name = field.getName();
        		String methodName = "get"+StringUtils.capitalize(name);
        		try {
					Object obj = entity.getClass().getMethod(methodName, new Class[0]);
					if (obj == null) {
						throw new IllegalArgumentException("no se puede actualizar un objeto sin id, debe utilizar save");
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }*/
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).save(entity);
	}

	@Override
	public void deleteAll() {
		morphia.createDatastore(factory.getDb().getMongo(), factory.getDb().getName()).
			getCollection(entityClass).drop();		
	}	
}
