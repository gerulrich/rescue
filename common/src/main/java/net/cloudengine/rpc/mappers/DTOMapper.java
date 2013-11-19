package net.cloudengine.rpc.mappers;

import java.util.Collection;
import java.util.List;

public interface DTOMapper {
	
	
	<T> T fillModel(Object entity, Class<T> modelClass);
	
	<T> List<T> fillModels(Collection<?> entities, Class<T> modelClass);
	
	<T> T fillEntity(Object model, Class<T> entityClass);
	
	<T> void fillEntity(Object model, T entity);
}
