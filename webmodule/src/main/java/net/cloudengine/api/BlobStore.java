package net.cloudengine.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface BlobStore {

	/**
	 * Borra el archivo del BlobStore.
	 * @param filename Nombre del archivo a borrar.
	 */
	void remove(String filename);
	
	/**
	 * Verifica si un archivo ya existe en el BlobStore.
	 * @param filename nombre del archivo
	 * @return true si el archivo existe, falso en caso contrario.
	 */
	boolean exists(String filename);
	
	/**
	 * 
	 * @return
	 */
	List<String> list();
	
	
	/**
	 * Almacena un nuevo archivo en el BlobStore.
	 * @param filename nombre del archivo.
	 * @param inputStream para guardar el archivo.
	 */
	void storeFile(String filename, InputStream inputStream);
	
	/**
	 * Obtiene un archivo del BlobStore
	 * @param filename nombre del archivo a obtener
	 * @param outputStream para leer el archivo.
	 */
	void retrieveFile(String filename, OutputStream outputStream);


}
