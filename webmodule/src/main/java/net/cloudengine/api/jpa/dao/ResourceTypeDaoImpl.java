package net.cloudengine.api.jpa.dao;

import org.springframework.stereotype.Repository;

import net.cloudengine.api.jpa.JPADatastore;
import net.cloudengine.model.resource.ResourceType;

@Repository
public class ResourceTypeDaoImpl extends JPADatastore<ResourceType, Long> implements ResourceTypeDao {

	public ResourceTypeDaoImpl() {
		super(ResourceType.class);
	}

	
}