package net.cloudengine.api.jpa.dao;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.resource.Resource;

public interface ResourceDao extends Datastore<Resource, Long> {

	Resource findByImei(String imei);
	
}
