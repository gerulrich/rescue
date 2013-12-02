package net.cloudengine.model.geo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="poi")
class POI {
	
	@Id ObjectId id;
	String name;
	String type;
	double x;
	double y;

}
