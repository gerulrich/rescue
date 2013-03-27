package net.cloudengine.api.mongo;

import java.util.List;

import net.cloudengine.api.PagingResult;

public class CollectionPagingResult<E> implements PagingResult<E> {

	private List<E> result;
	private int pageNumber;
	private int pageSize;
	private long totalPages;
	private long totalSize;
	
	public CollectionPagingResult(List<E> result, int pageNumber, int pageSize, long totalPages, long totalSize) {
		this.result = result;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalPages = totalPages;
		this.totalSize = totalSize;
	}
	
	@Override
	public long getPageNumber() {
		return pageNumber;
	}

	@Override
	public long getPageSize() {
		return pageSize;
	}

	@Override
	public long getTotalSize() {
		return totalSize;
	}

	@Override
	public long getTotalPages() {
		return totalPages;
	}

	@Override
	public List<E> getList() {
		return result;
	}

	@Override
	public List<E> getCompleteList() {
		throw new RuntimeException("Método ni implementado, por favor utilice getList() en su lugar");
	}

}
