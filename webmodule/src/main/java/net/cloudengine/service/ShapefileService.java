package net.cloudengine.service;

import java.util.Map;

import org.apache.commons.fileupload.ProgressListener;

import net.cloudengine.model.commons.FileDescriptor;

public interface ShapefileService {
	
	// constantes para los nombre de los campos de los shapefiles 
	public static final String NOMBRE = "NOMBRE";
	public static final String TIPO = "TIPO";
	public static final String ALT_II = "ALT_II";
	public static final String ALT_IF = "ALT_IF";
	public static final String ALT_DI = "ALT_DI";
	public static final String ALT_DF = "ALT_DF";
	public static final String VINICIO = "VINICIO";
	public static final String VFIN = "VFIN";
	
	/**
	 * Carga un shapefile de puntos como puntos de interes.
	 * @param descriptor 
	 * @param nameField campo del shapefile que contiene el nombre
	 * @param typeField campo del shapefile que contiene el tipo
	 * @param listener indicar de progreso del proceso de carga. 
	 * @return cantidad de objetos geo-espaciales cargados. 
	 */
	public long shp2Poi(FileDescriptor descriptor, String nameField, String typeField, boolean overwrite, ProgressListener listener);
	
	/**
	 * Carga un shapefile de puntos como puntos de interes.
	 * @param descriptor 
	 * @param fieldNames Map con los nombres
	 * @param typeField campo del shapefile que contiene el tipo
	 * @param listener indicar de progreso del proceso de carga. 
	 * @return cantidad de objetos geo-espaciales cargados.
	 */
	public long shp2Street(FileDescriptor descriptor, Map<String,String> fieldNames, boolean overwrite, ProgressListener listener);

	/**
	 * Carga un shapefile de puntos como puntos de interes.
	 * @param descriptor 
	 * @param fieldNames Map con los nombres
	 * @param typeField campo del shapefile que contiene el tipo
	 * @param listener indicar de progreso del proceso de carga.
	 * @return cantidad de objetos geo-espaciales cargados.
	 */
	public long shp2Zone(FileDescriptor descriptor, String nameField, String type, boolean overwrite, ProgressListener listener);
	
	/**
	 * Lee los nombres de los campos asociados al shapefile
	 * @param descriptor
	 * @param nameField
	 * @param typeField
	 * @return
	 */
	public String[] readFileFields(FileDescriptor descriptor);

}
