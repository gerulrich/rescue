package net.cloudengine.api.jpa.dao;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.map.StreetBlock;
import net.cloudengine.model.map.StreetIntersection;

public interface StreetBlockDao extends Datastore<StreetBlock, Long> {
	
	StreetBlock find(String loc, Integer nro);
	
	StreetIntersection find(String loc1, String loc2);

}
