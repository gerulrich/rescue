package net.cloudengine.xml;

import groovy.util.Node;
import groovy.util.XmlParser;

import java.text.SimpleDateFormat

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.cloudengine.AppListener;

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject;
import com.mongodb.DBRef
import com.mongodb.Mongo
import com.mongodb.util.JSON;


class DataReader {
	
	static Logger logger = LoggerFactory.getLogger(DataReader.class);
	
	DB mongoDB;
	
	def loadAppData(DB db, String file) {
		this.mongoDB = db;
		StringBuffer sb = new StringBuffer();
		URL url = DataReader.class.getResource(file)
		File f = new File(url.toURI())
		f.eachLine() { String line ->
			sb.append (line);
		}
		
		def records = new XmlParser().parseText(sb.toString())
		records.each { Node node ->
			String colName = node.name();
			DBCollection collection = db.getCollection(colName);

			DBObject doc = new BasicDBObject();
			
			String type = node.'@type';
			if ("json".equals(type)) {
				doc = (DBObject) JSON.parse(node.text());
			} else {
				node.children().each { Node child ->
					processNode(child, doc, true);
				}
			}
			
			String id = node.'@id';
			String idValue = doc.getString(id);
			
			BasicDBObject query = new BasicDBObject();
			query.put(id, idValue);
			long cantidad = collection.count(query);
			
			if (cantidad == 0) {
				logger.debug('Insertando dato inicial '+node.name()+' '+id+'='+idValue);
				collection.insert(doc);
			} else {
				logger.debug('dato inicial ya existente '+node.name()+' '+id+'='+idValue);
			}
			
		}
	}

	
	def processNode(Node node, BasicDBObject object, boolean root) {
		if (node.children().size() == 1 && node.children().get(0) instanceof String) {
			
			def child = node.children().get(0);
			String type = node.'@type'; 
			if ('boolean'.equals(type)) {
				child = Boolean.valueOf(child);
			}
			object.put(node.name(), child)
//		} else {
//			if (!root) {
//				node.children().each { child ->
//					processNode(child, object, false);
//				}
		} else {
			String collectionRef = node.'@reference';
			if (StringUtils.isNotEmpty(collectionRef)) {
				
				String id = node.'@id';
				String value = node.'@value';
				DBCollection dbCollection = this.mongoDB.getCollection(collectionRef);
				
				BasicDBObject query = new BasicDBObject();
				query.put(id, value);
				DBObject dbObject = dbCollection.findOne(query);
				
				ObjectId idRef = (ObjectId) dbObject.get("_id");
				DBRef ref = new DBRef(mongoDB, collectionRef, idRef);
				
				if (object.get(node.name())==null) {
					object.put(node.name(), new ArrayList());
				}
				object.get(node.name()).add(ref);
				
			} else {
				BasicDBObject embeedd = new BasicDBObject();
				node.children().each { child ->
					processNode(child, embeedd, true);
				}
				object.put(node.name(), embeedd);
			}
		}
//		}
	}
		
}
