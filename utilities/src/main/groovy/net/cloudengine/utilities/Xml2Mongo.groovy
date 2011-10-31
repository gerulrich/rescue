package net.cloudengine.utilities

import groovy.util.Node;
import groovy.util.XmlParser;

import java.text.SimpleDateFormat

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject;
import com.mongodb.Mongo
import com.mongodb.util.JSON;


class Xml2Mongo {
	
	static void main(def args) {
		
		// FIXME sacar harcoding
		Mongo mongo = new Mongo()
		DB db = mongo.getDB("webadmin")
		db.dropDatabase();
		db = mongo.getDB("webadmin")
		
		StringBuffer sb = new StringBuffer();
		URL url = Xml2Mongo.class.getResource("/net/cloudengine/utilities/data.xml")
		File f = new File(url.toURI())
		f.eachLine() { String line ->
			sb.append (line);
		}
	  
	  
		def oldColdName = ''
	  
		def records = new XmlParser().parseText(sb.toString())
		records.each { Node node ->
		  String colName = node.name();
		  DBCollection collection = db.getCollection(colName); 
		  if (!oldColdName.equals(colName)) {
			  collection.drop();
			  oldColdName = colName;
		  }
		  
		  BasicDBObject doc = new BasicDBObject();
		  
		  node.children().each { Node child ->
			  processNode(child, doc, true);
		  }
		  
		  println 'Insertando '+node.name();
		  collection.insert(doc);
		  
	  }
		
		mongo.close();
		
	}
	
	static def processNode(Node node, BasicDBObject object, boolean root) {
		if (node.children().size() == 1 && node.children().get(0) instanceof String) {
			
			def child = node.children().get(0);
			object.put(node.name(), child)
			
		} else {
			if (!root) {
				node.children().each { child ->
					processNode(child, object, false);
				}
			} else {
				BasicDBObject embeedd = new BasicDBObject();
				node.children().each { child ->
					processNode(child, embeedd, true);
				}
				object.put(node.name(), embeedd);
			}
		}
	}
		
}
