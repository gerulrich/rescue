package net.cloudengine.service;

import java.util.Collection;

import net.cloudengine.dao.support.Page;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.model.resource.Resource;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.model.resource.WayPoint;

import org.bson.types.ObjectId;

/**
 * Servicio para Recursos y Tipos de recursos.
 * @author German
 *
 */
public interface ResourceService {
	
	/**
	 * Obtiene un {@link ResourceType} por su id.
	 * @param id
	 * @return
	 */
	Resource get(Long id);

	/**
	 * Guarda un nuevo {@link Resource}.
	 * @param resource recurso a guardar.
	 */
	void save(Resource resource);
	
	/**
	 * Actualiza un {@link Resource} ya existente
	 * @param resource recurso a guardar.
	 */
	void update(Resource resource);	
	
	/**
	 * Obtiene una lista de todos los {@link Resource}
	 * @param page
	 * @param size
	 * @return
	 */
	Page<Resource> getAll(int page, int size);
	
	/**
	 * Obtiene una lista de todos los {@link Resource} cuya versión
	 * es mayor a la especificada.
	 * @param version
	 * @return
	 */
	Collection<Resource> getAll(long version);	
	
	
	/**
	 * Obtiene un {@link Resource} por su imei.
	 * @param imei
	 * @return
	 */
	Resource getByImei(String imei);
	
	/**
	 * Obtiene un {@link ResourceType} por su id.
	 * @param id
	 * @return
	 */
	ResourceType getType(Long id);
	
	/**
	 * Guarda un {@link ResourceType} nuevo.
	 * @param type tipo a guardar
	 * @param imageFile imagen o icono para el tipo.
	 * 
	 */
	void save(ResourceType type);
	
	/**
	 * Actualiza un {@link ResourceType} ya existente.
	 * @param type
	 */
	void update(ResourceType type);
	
	/**
	 * Elimina un tipo de recurso
	 * @param id
	 */
	void deleteType(Long id);
	
	/**
	 * Elimina un recurso
	 * @param id
	 */
	void delete(Long id);
	
	/**
	 * Obtiene una lista de todos los {@link ResourceType}
	 * @param page
	 * @param size
	 * @return
	 */
	Page<ResourceType> getAllTypes(int page, int size);
	
	/**
	 * Obtiene una lista de todos los {@link ResourceType}
	 * @return
	 */
	Collection<ResourceType> getAllTypes();	
	
	/**
	 * Obtiene una lista de todas las imagenes
	 * que se pueden asignar a los {@link ResourceType}.
	 * @return
	 */
	Collection<FileDescriptor> getAllImages();
	
	/**
	 * Obtiene una imagen por su id.
	 * @param id
	 * @return
	 */
	FileDescriptor getImage(ObjectId id);
	
	void save(WayPoint wayPoint);

}
