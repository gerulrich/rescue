package net.cloudengine.api.jpa.dao;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.map.Street;

public interface StreetDao extends Datastore<Street, Long> {
	
	Street find(String loc, Integer nro);

}
