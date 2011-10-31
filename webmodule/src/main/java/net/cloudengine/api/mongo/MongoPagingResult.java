package net.cloudengine.api.mongo;

import java.util.Collection;

import net.cloudengine.api.PagingResult;
import net.cloudengine.util.Assert;

import com.google.code.morphia.Datastore;

public final class MongoPagingResult<E> implements PagingResult<E> {

	private Class<E> entityClass;
	private Datastore datastore;
	private int startIndex;
//	private int endIndex;
	private int pageSize;
	private int pageNumber;
	
	// Valores calculados.
	private long totalPages = -1;
	private long totalSize = -1;
	
	public MongoPagingResult(Class<E> entityClass, Datastore datastore, int pageNumber, int pageSize) {
		Assert.isTrue(pageNumber > 0, "error.page.number");
		Assert.isTrue(pageSize > 0, "error.page.size");
		Assert.notNull(entityClass, "");
		
		this.entityClass = entityClass;
		this.datastore = datastore;
		this.pageSize = pageSize;
		this.startIndex = (pageNumber-1)*pageSize;
//		this.endIndex = (pageNumber+1)*pageSize;
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
		if ( (pageNumber+1) == getTotalPages())
			return getTotalSize();
					
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
			this.totalSize = datastore.getCount(entityClass);
		}
		return this.totalSize;
	}

	@Override
	public Collection<E> getList() {
		return datastore.find(entityClass).offset(startIndex).limit(pageSize).asList();
	}

}
