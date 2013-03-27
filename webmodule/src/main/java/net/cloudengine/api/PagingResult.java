package net.cloudengine.api;

import java.util.List;

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
	List<E> getList();
	
	/***
	 * Retorna la lista con todos los resultados, no solo los 
	 * de la página actual. Es recomendable su uso solo 
	 * para conjuntos de resultados pequeños.
	 * @return
	 */
	List<E> getCompleteList();
	
	
}
