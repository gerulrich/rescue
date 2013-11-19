package net.cloudengine.api.jpa.dao;

import java.util.List;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.geo.Zone;

public interface ZoneDao extends Datastore<Zone, Long> {
	
	List<Zone> getByType(String type);
	List<Zone> getByNameAndType(String name, String type);
	List<String> getZonesType();

}
