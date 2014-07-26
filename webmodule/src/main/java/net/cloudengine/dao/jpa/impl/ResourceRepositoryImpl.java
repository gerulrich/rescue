package net.cloudengine.dao.jpa.impl;

import java.util.Collection;

import net.cloudengine.dao.jpa.ResourceRepository;
import net.cloudengine.model.resource.Resource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceRepositoryImpl extends JPARepository<Resource, Long> implements ResourceRepository {

	public ResourceRepositoryImpl() {
		super(Resource.class);
	}
	
	public Collection<Resource> getResourceWithGreaterVersion(long version) {
		Criteria criteria = this.getHibernateSession().createCriteria(Resource.class);
		criteria.add(Restrictions.gt("version", version));
		return cast(criteria.list());
	}

}
