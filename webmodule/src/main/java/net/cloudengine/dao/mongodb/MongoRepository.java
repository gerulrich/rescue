package net.cloudengine.dao.mongodb;

import java.io.Serializable;
import java.util.List;

import net.cloudengine.dao.support.Page;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.SearchParameters;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class MongoRepository<E,PK extends Serializable> implements Repository<E, PK> {

	private Class<E> persistentClass;
	protected MongoTemplate mongoTemplate;
	
	public MongoRepository(Class<E> persistentClass, MongoTemplate mongoTemplate) {
		this.persistentClass = persistentClass;
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public E get(PK id) {
		return mongoTemplate.findById(id, this.persistentClass);
	}

	@Override
	public <T> void save(T entity) {
		this.mongoTemplate.save(entity);		
	}

	@Override
	public List<E> getAll() {
		return mongoTemplate.findAll(this.persistentClass);
	}

	@Override
	public Page<E> list() {
		return list(FIRST_PAGE, PAGE_SIZE);
	}

	@Override
	public Page<E> list(int page, int size) {
		int startIndex = (page-1)*page;
		long totalSize = mongoTemplate.getCollection(mongoTemplate.getCollectionName(persistentClass)).count();
		Query query = new Query().skip(startIndex).limit(size);
		List<E> list = mongoTemplate.find(query, this.persistentClass);
		return new MongoPage<E>(list, page, size, totalSize);
	}

	@Override
	public <T> void update(T entity) {
		this.mongoTemplate.save(entity);
	}

	@Override
	public void delete(PK key) {
		mongoTemplate.remove(get(key));//FIXME		
	}

	@Override
	public Class<E> getType() {
		return this.persistentClass;
	}

	@Override
	public void deleteAll() {
		mongoTemplate.remove(new Query(), this.persistentClass);
	}

	@Override
	public Page<E> find(SearchParameters searchParameters, int page, int size) {
		int startIndex = (page-1)*page;
		Query query = MongoUtils.createQuery(searchParameters);
		long totalSize = mongoTemplate.count(query, this.persistentClass);
		query.skip(startIndex).limit(size);
		List<E> list = mongoTemplate.find(query, this.persistentClass); 
		return new MongoPage<E>(list, page, size, totalSize);
	}

	@Override
	public Page<E> list(SearchParameters searchParameters) {
		return find(searchParameters, FIRST_PAGE, PAGE_SIZE);
	}

	@Override
	public E findOne(SearchParameters searchParameters) {
		Query query = MongoUtils.createQuery(searchParameters);
		return mongoTemplate.findOne(query, this.persistentClass);
	}
}
