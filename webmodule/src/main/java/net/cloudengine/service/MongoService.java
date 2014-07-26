package net.cloudengine.service;

import java.util.List;
import java.util.Set;

import net.cloudengine.dao.support.Page;
import net.cloudengine.model.console.MongoCollection;
import net.cloudengine.model.console.MongoObject;

public interface MongoService {
	
	List<MongoCollection> getCollections();
	
	boolean isValidCollection(String collection);
	
	void drop(String collection);
	
	Set<String> getHeaders(String collection, int page, int size);
	
	Page<MongoObject> getObjects(String collection, int page, int size);

}
