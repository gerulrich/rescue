package net.cloudengine.api.mongo;

import java.util.List;

import net.cloudengine.api.PagingResult;
import net.cloudengine.validation.Assert;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

//import com.google.code.morphia.Datastore;

public final class MongoPagingResult<E> implements PagingResult<E> {

	private Class<E> entityClass;
	private int startIndex;
	private MongoTemplate mongoTemplate;
	private int pageSize;
	private int pageNumber;
	
	// Valores calculados.
	private long totalPages = -1;
	private long totalSize = -1;
	
	public MongoPagingResult(MongoTemplate mongoTemplate, Class<E> entityClass, int pageNumber, int pageSize) {
		Assert.isTrue(pageNumber > 0, "error.page.number");
		Assert.isTrue(pageSize > 0, "error.page.size");
		Assert.notNull(entityClass, "");
		this.mongoTemplate = mongoTemplate;
		this.entityClass = entityClass;
		this.pageSize = pageSize;
		this.startIndex = (pageNumber-1)*pageSize;
		this.pageNumber = pageNumber;
		
	}
	
	@Override
	public long getPageNumber() {
		return pageNumber;
	}

	@Override
	public long getPageSize() {
		// FIXME fijarse en el caso de que es la ultima página puede tener
		// menos resultados que el máximo por página.
//		if ( (pageNumber+1) == getTotalPages())
//			return getTotalSize();
		return pageSize;
	}
	
	public long getTotalPages() {
		if (this.totalPages < 0) {
			long total = getTotalSize();
			this.totalPages = total / pageSize;
			if (total % pageSize != 0)
				this.totalPages++;
		}
		return this.totalPages;
	}

	@Override
	public long getTotalSize() {
		if (this.totalSize < 0) {
			this.totalSize = mongoTemplate.getCollection(mongoTemplate.getCollectionName(entityClass)).count();
		}
		return this.totalSize;
	}

	@Override
	public List<E> getList() {
		Query query = new Query();
		query.skip(startIndex).limit(pageSize);
		return mongoTemplate.find(query, entityClass);
	}

	@Override
	public List<E> getCompleteList() {
		return mongoTemplate.findAll(entityClass);
	}

}
