package net.cloudengine.dao.mongodb;

import java.util.List;

import net.cloudengine.dao.support.Page;

public class MongoPage<E> implements Page<E> {

	private List<E> list;
	private long pageNumber;
	private long pageSize;
	private long totalSize;
	
	public MongoPage(List<E> list, long pageNumber, long pageSize, long totalSize) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalSize = totalSize;
		this.list = list;
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
		return totalSize / pageSize;
	}

	@Override
	public List<E> getList() {
		return list;
	}
}
