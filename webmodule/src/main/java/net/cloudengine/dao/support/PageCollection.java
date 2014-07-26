package net.cloudengine.dao.support;

import java.util.List;

public class PageCollection<E> implements Page<E> {

	private List<E> result;
	private int pageNumber;
	private int pageSize;
	private long totalPages;
	private long totalSize;
	
	public PageCollection(List<E> result, int pageNumber, int pageSize, long totalSize) {
		this.result = result;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalPages = -1;
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
		if (totalPages <= 0) {
			totalPages = totalSize/pageSize;
			if (totalSize % pageSize != 0 || totalPages == 0) {
				totalPages++;
			}
		}
		return totalPages;
	}

	@Override
	public List<E> getList() {
		return result;
	}
}
