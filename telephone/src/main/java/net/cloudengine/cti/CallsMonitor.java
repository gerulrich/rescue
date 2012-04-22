package net.cloudengine.cti;

public interface CallsMonitor {
	
	/**
	 * Agrega un listener para eventos de llamada.
	 * @param listener
	 */
	void addListener(CallListener listener);
	
	/**
	 * Elimina un listener.
	 * @param listener
	 */
	void removreListener(CallListener listener);
	
	/**
	 * Finaliza una llamada en curso.
	 * @param call llamada a finalizar.
	 */
	void hungap(Call call);
	
	/**
	 * Peticion para indicar que se quiere grabar una llamada en curso.
	 * @param call llamada a grabar
	 */
	void record(Call call);

}
