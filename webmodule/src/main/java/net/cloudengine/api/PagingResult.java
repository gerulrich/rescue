package net.cloudengine.api;

import java.util.Collection;

public interface PagingResult<E> {
	
	/**
	 * Retorna el número de página.
	 * @return
	 */
	long getPageNumber();
	
	/**
	 * Retorna la cantidad de resultados en la página. 
	 * @return 
	 */
	long getPageSize();
	
	/**
	 * Retorna la cantidad de resultados.
	 * @return
	 */
	long getTotalSize();
	
	long getTotalPages();
	
	Collection<E> getList();
	
	
}
