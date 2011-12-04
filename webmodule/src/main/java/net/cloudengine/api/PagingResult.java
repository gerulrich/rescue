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
	 * Retorna la cantidad de resultados totales, sin tener 
	 * en cuenta el paginado.
	 * @return
	 */
	long getTotalSize();
	
	/**
	 * Retorna la cantidad de paginas totales.
	 * @return
	 */
	long getTotalPages();
	
	/**
	 * Retorna la lista con los resultados de la
	 * página actual.
	 * @return
	 */
	Collection<E> getList();
	
	
}
