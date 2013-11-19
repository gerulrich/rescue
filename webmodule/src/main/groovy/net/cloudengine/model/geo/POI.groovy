package net.cloudengine.model.geo

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id;

@Entity("poi")
class POI {
	
	@Id
	ObjectId id;
	String name;
	String type;
	double x;
	double y;

}
