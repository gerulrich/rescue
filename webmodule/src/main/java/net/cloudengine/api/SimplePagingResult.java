package net.cloudengine.api;

import java.util.List;

public class SimplePagingResult<E> implements PagingResult<E> {

	private long pageNumber;
	private long pageSize;
	private long totalPages;
	private long totalSize;
	private List<E> list;
	
	public SimplePagingResult(long pageNumber, long pageSize, long totalSize, List<E> list) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalSize = totalSize;
		this.list = list;
		this.totalPages = totalSize / pageSize;
		if (totalSize % pageSize != 0)
			this.totalPages++;		
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
		return list;
	}

	@Override
	public List<E> getCompleteList() {
		throw new RuntimeException("Metodo no implementado, utilice getList()");
	}

}
