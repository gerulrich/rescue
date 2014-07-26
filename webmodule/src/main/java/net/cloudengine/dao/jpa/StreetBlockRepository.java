package net.cloudengine.dao.jpa;

import java.util.Collection;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.model.geo.StreetBlock;
import net.cloudengine.model.geo.StreetIntersection;

public interface StreetBlockRepository extends Repository<StreetBlock, Long> {
	
	Collection<StreetBlock> find(String loc, Integer nro);
	
	StreetIntersection find(String loc1, String loc2);
	
	StreetBlock findStreetBlockByStartVertix(int startVertix);

}
