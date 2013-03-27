package net.cloudengine.api.jpa.dao;

import org.springframework.stereotype.Repository;

import net.cloudengine.api.jpa.JPADatastore;
import net.cloudengine.model.resource.Resource;

@Repository
public class ResourceDaoImpl extends JPADatastore<Resource, Long> implements ResourceDao {

	public ResourceDaoImpl() {
		super(Resource.class);
	}
	
	public Resource findByImei(String imei) {
		return null;
	}

}
