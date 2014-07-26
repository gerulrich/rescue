package net.cloudengine.dao.jpa;

import java.util.List;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.model.geo.Zone;

public interface ZoneRepository extends Repository<Zone, Long> {
	
	List<Zone> getByType(String type);
	List<Zone> getByNameAndType(String name, String type);
	List<String> getZonesType();

}