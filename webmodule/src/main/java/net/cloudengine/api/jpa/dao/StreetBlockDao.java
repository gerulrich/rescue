package net.cloudengine.api.jpa.dao;

import java.util.Collection;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.geo.StreetBlock;
import net.cloudengine.model.geo.StreetIntersection;

public interface StreetBlockDao extends Datastore<StreetBlock, Long> {
	
	Collection<StreetBlock> find(String loc, Integer nro);
	
	StreetIntersection find(String loc1, String loc2);

}
