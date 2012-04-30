package net.cloudengine.mappers;
import java.util.Collection;
import java.util.List;

public interface DTOMapper {
	
	
	<T> T fillModel(Object entity, Class<T> modelClass);
	
//	void fillEntity(Object model, Object entity);
	
	
//	<T> T fillEntity(Object model, Class<T> entityClass);
	
	
	<T> List<T> fillModels(Collection<?> entities, Class<T> modelClass);
	
}
