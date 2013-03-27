package net.cloudengine.model.resource

import org.bson.types.ObjectId

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id

/**
 * Representa coordenadas para ubicar puntos de referencia para 
 * trazar rutas mediante una secuencia de puntos.
 * @author German
 *
 */
@Entity("waypoint")
class WayPoint {
	
	@Id ObjectId id;
	Date date = new Date();
	String location;
	Long resource;
	double speed;
	double lon;
	double lat;

}
