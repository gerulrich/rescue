package net.cloudengine.web.admin;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.document.mongodb.MongoDbFactory;
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
	
	@RequestMapping(value="/admin/mongo/list", method = RequestMethod.GET)
	public ModelAndView mongoCollections() {
		ModelAndView mav = new ModelAndView();
		
		List<MongoCollection> collections = new ArrayList<MongoCollection>();
		for (String colName : db.getCollectionNames()) {
			if (colName != null && !colName.startsWith("system.")) {
				DBCollection collection = db.getCollection(colName);
//				collection.
				
				long count = collection.count();
				MongoCollection mcol = new MongoCollection(colName, count);
				collections.add(mcol);
			}
		}
		mav.addObject("collections", collections);
		mav.setViewName("/mongo/collections");
		return mav;
	}
	
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
		mav.setViewName("redirect:/admin/mongo/show/{collection}/1/100");
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
		
		List<MongoObject> objects = new ArrayList<MongoObject>();
		
		for (DBObject object : cursor) {
			MongoObject obj = new MongoObject();
			obj.setValues(object.toMap());
			objects.add(obj);
		}
		
		mav.addObject("collection", collection);
		mav.addObject("objects", objects);
		if (!objects.isEmpty()) {
			mav.addObject("headers", objects.get(0).getPropertiesNames());
		}
		mav.setViewName("/mongo/collection");
		return mav;
	}
	
	

}
