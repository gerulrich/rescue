package net.cloudengine.web.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.cloudengine.api.mongo.CollectionPagingResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
/**
 * Mustra información basica de la DB de Mongo.
 * @author German Ulrich
 *
 */
@Controller
public class MongoController {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoController.class);
	private DB db;

	@Autowired
	public MongoController(MongoDbFactory factory) {
		super();
		this.db = factory.getDb();
	}
	
	/**
	 * Obtiene la lista de colecciones de la base de datos de MongoDB.
	 */
	@RequestMapping(value="/admin/mongo/list", method = RequestMethod.GET)
	public ModelAndView mongoCollections() {
		ModelAndView mav = new ModelAndView();
		
		List<MongoCollection> collections = new ArrayList<MongoCollection>();
		for (String colName : db.getCollectionNames()) {
			if (colName != null && !colName.startsWith("system.") && !colName.startsWith("fs.")) {
				DBCollection collection = db.getCollection(colName);
				MongoCollection mcol = new MongoCollection(colName, collection.getStats());
				collections.add(mcol);
			}
		}
		mav.addObject("collections", collections);
		mav.setViewName("/mongo/collections");
		return mav;
	}
	
	/**
	 * Elimina la coleccion de la base de datos de MongoDB.
	 */
	@RequestMapping(value="/admin/mongo/drop/{collection}", method = RequestMethod.GET)
	public ModelAndView dropCollection(@PathVariable("collection") String collection) {
		ModelAndView mav = new ModelAndView();
		if (collection != null && !collection.startsWith("system.")) {
			logger.debug("Borrando colección {} de la base de mongodb", collection);
			db.getCollection(collection).drop();
		}
		mav.setViewName("redirect:/admin/mongo/list");
		return mav;
	}
	
	
	@RequestMapping(value="/admin/mongo/show/{collection}", method = RequestMethod.GET)
	public ModelAndView showCollection(@PathVariable("collection") String collection) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/admin/mongo/show/{collection}/1/25");
		return mav;
	}
	
	@RequestMapping(value="/admin/mongo/show/{collection}/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView showCollection(@PathVariable("collection") String collection, @PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		if (collection != null && collection.startsWith("system.")) {
			mav.setViewName("redirect:/admin/mongo/list");
			return mav;
		}
		
		DBCollection colDB = db.getCollection(collection);
		DBCursor cursor = colDB.find().skip((page-1)*size).limit(size);
		long totalPages = totalPages(colDB, page, size);
		
		List<MongoObject> objects = new ArrayList<MongoObject>();
		
		Set<String> headers = new TreeSet<String>(); 
		
		for (DBObject object : cursor) {
			headers.addAll(object.keySet());
			MongoObject obj = new MongoObject();
			Map<String,Object> values = new HashMap<String, Object>();
			for (String key : object.keySet()) {
				values.put(key, object.get(key));
			}
			obj.setValues(values);
			objects.add(obj);

		}
		
		headers.remove("className");
		headers.remove("password");
		
		mav.addObject("collection", collection);
		mav.addObject("objects", new CollectionPagingResult<MongoObject>(objects, page, size, totalPages, colDB.count()));
		mav.addObject("headers", headers);
		mav.setViewName("/mongo/collection");
		return mav;
	}
	
	private long totalPages(DBCollection colDB, int page, int size) {
		long total = colDB.count();
		long totalPages = total / size;
		if (total % size != 0)
			totalPages++;
		return totalPages;
	}
}
