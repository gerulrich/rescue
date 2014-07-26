package net.cloudengine.dao.jpa.impl;

import net.cloudengine.dao.jpa.ResourceTypeRepository;
import net.cloudengine.model.resource.ResourceType;

import org.springframework.stereotype.Repository;

@Repository
public class ResourceTypeDaoImpl extends JPARepository<ResourceType, Long> implements ResourceTypeRepository {

	public ResourceTypeDaoImpl() {
		super(ResourceType.class);
	}

	
}