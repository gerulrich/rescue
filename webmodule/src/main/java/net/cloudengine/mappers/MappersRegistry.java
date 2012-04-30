package net.cloudengine.mappers;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MappersRegistry {
	
	private ConcurrentMap<String, DTOMapper> mappers = new ConcurrentHashMap<String, DTOMapper>();
	
	public void setMappers(Map<String, DTOMapper> map) {
		Set<String> keys = map.keySet(); 
		for (String className : keys) {
			this.mappers.put(className, map.get(className));
		}
	}

	public DTOMapper getMapper(Class<?> modelClass) {
		DTOMapper mapper = mappers.get(modelClass.getName());
		if (mapper == null) {
			mapper = new BasicMapper();
		}
		return mapper;
	}
}
