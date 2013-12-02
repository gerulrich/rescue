package net.cloudengine.model.resource

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Representa coordenadas para ubicar puntos de referencia para 
 * trazar rutas mediante una secuencia de puntos.
 * @author German
 *
 */
@Document(collection="waypoint")
class WayPoint {
	
	@Id ObjectId id;
	Date date = new Date();
	String location;
	Long resource;
	double speed;
	double lon;
	double lat;

}
