package net.cloudengine.service.map;

import net.cloudengine.model.commons.FileDescriptor;

public interface ShapefileService {
	
	
	/**
	 * Carga un shapefile de puntos como puntos de interes.
	 * @param descriptor 
	 * @param nameField campo del shapefile que contiene el nombre
	 * @param typeField campo del shapefile que contiene el tipo
	 * @return
	 */
	public long shp2Poi(FileDescriptor descriptor, String nameField, String typeField, boolean overwrite);
	
//	public void shp2Region(FileDescriptor descriptor, String nameField, String typeField);
	public void shp2Street(FileDescriptor descriptor, String nameField, String typeField, 
			String fromLeftField, String toLeftField, String fromRightField, String toRightField);
	
	
	/**
	 * Lee los nombres de los campos asociados al shapefile
	 * @param descriptor
	 * @param nameField
	 * @param typeField
	 * @return
	 */
	public String[] readFileFields(FileDescriptor descriptor);

}
