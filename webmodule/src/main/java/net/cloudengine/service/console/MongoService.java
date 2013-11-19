package net.cloudengine.service.console;

import java.util.List;
import java.util.Set;

import net.cloudengine.api.PagingResult;
import net.cloudengine.model.console.MongoCollection;
import net.cloudengine.model.console.MongoObject;

public interface MongoService {
	
	List<MongoCollection> getCollections();
	
	boolean isValidCollection(String collection);
	
	void drop(String collection);
	
	Set<String> getHeaders(String collection, int page, int size);
	
	PagingResult<MongoObject> getObjects(String collection, int page, int size);

}
