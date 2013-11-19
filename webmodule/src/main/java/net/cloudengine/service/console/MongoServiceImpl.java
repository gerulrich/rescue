package net.cloudengine.service.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.cloudengine.api.PagingResult;
import net.cloudengine.api.mongo.CollectionPagingResult;
import net.cloudengine.model.console.MongoCollection;
import net.cloudengine.model.console.MongoObject;

import org.bson.BasicBSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

@Service
public class MongoServiceImpl implements MongoService {

	private static final Logger logger = LoggerFactory.getLogger(MongoServiceImpl.class);
	private DB db;

	@Autowired
	public MongoServiceImpl(MongoDbFactory factory) {
		super();
		this.db = factory.getDb();
	}
	
	@Override
	public List<MongoCollection> getCollections() {
		logger.debug("Obteniendo lista de collections de MongoDB");
		List<MongoCollection> collections = new ArrayList<MongoCollection>();
		for (String colName : db.getCollectionNames()) {
			if (!isPrivate(colName)) {
				DBCollection collection = db.getCollection(colName);
				MongoCollection mcol = new MongoCollection(colName, collection.getStats());
				collections.add(mcol);
			}
		}
		return collections;
	}
	
	@Override
	public boolean isValidCollection(String collection) {
		return !isPrivate(collection);
	}

	@Override
	public void drop(String collection) {
		if (!isPrivate(collection)) {
			logger.debug("Borrando collection {} de la base de mongodb", collection);
			db.getCollection(collection).drop();
		}
	}
	
	
	//FIXME buscar otra forma de obtener los campos porque es medio lento.
	@Override
	public Set<String> getHeaders(String collection, int page, int size) {
		logger.debug("Obteniendo campos de la collection {} de MongoDB", collection);
		
		DBCollection colDB = db.getCollection(collection);
		DBObject query = colDB.find().skip((page-1)*size).limit(size).getQuery();
		
		String map = "function () { for (var key in this) { emit(key, null); }; }";
		String reduce = "function(key, stuff) { return null; }";

		DBCollection col = db.getCollection (collection);
		MapReduceCommand command = new MapReduceCommand(col, map, reduce, null, MapReduceCommand.OutputType.INLINE, query);
		
		MapReduceOutput out = col.mapReduce(command);
		
		Set<String> fields = new TreeSet<String>();
		for (DBObject ro : out.results()) {
			BasicBSONObject object = (BasicBSONObject)ro; 
			String field = object.getString("_id");
			if (field != null && !field.equals("className") && !field.equals("password")) {
				fields.add(field);
			}
		}
		return fields;
	}

	@Override
	public PagingResult<MongoObject> getObjects(String collection, int page, int size) {
		Object params[] = {collection, page, size};
		logger.debug("Obteniendo datos de la collection {} de MongoDB, page {}, size {}", params);
		
		DBCollection colDB = db.getCollection(collection);
		DBCursor cursor = colDB.find().skip((page-1)*size).limit(size);
		long totalPages = totalPages(colDB, page, size);
		
		List<MongoObject> objects = new ArrayList<MongoObject>();
		
		for (DBObject object : cursor) {
			MongoObject obj = new MongoObject();
			Map<String,Object> values = new HashMap<String, Object>();
			for (String key : object.keySet()) {
				values.put(key, object.get(key));
			}
			obj.setValues(values);
			objects.add(obj);

		}		
		return new CollectionPagingResult<MongoObject>(objects, page, size, totalPages, colDB.count());
	}

	/**
	 * Verifica si es una collection privada, es decir si es de sistema o del file system.
	 * @param collection nombre de la collection
	 * @return
	 */
	private boolean isPrivate(String collection) {
		return !(collection != null && !collection.startsWith("system.") && !collection.startsWith("fs."));
	}
	
	/**
	 * Calcula la cantidad de p&aacute;ginas en funcion del tama&ntilde;o de la 
	 * p&aacute;gina y la cantidad de registros.
	 * @param colDB nombre de la collection de MongoDB.
	 * @param page n&uacute;mero de p&aacute;gina.
	 * @param size tama&ntilde;o de p&aacute;gina.
	 * @return
	 */
	private long totalPages(DBCollection colDB, int page, int size) {
		long total = colDB.count();
		long totalPages = total / size;
		if (total % size != 0)
			totalPages++;
		return totalPages;
	}

}
