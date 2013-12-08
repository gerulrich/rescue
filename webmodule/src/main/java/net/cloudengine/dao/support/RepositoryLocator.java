package net.cloudengine.dao.support;

import static com.google.common.collect.Maps.newHashMap;
import groovy.lang.Singleton;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Singleton
@Service
public class RepositoryLocator {
	
	private Map<Class<?>, Repository<?, ?>> repositories = newHashMap();
	
	@Autowired
    void buildCache(List<Repository<?, ?>> registredRepositories) {
        for (Repository<?, ?> repository : registredRepositories) {
            repositories.put(repository.getType(), repository);
        }
    }
	
    @SuppressWarnings("unchecked")
    public  <T, ID extends Serializable> Repository<T,ID> getRepository(Class<T> clazz) {
        return (Repository<T,ID>) repositories.get(clazz);
    }
	
	
}
