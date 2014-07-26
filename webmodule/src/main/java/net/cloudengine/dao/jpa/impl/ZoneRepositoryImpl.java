package net.cloudengine.dao.jpa.impl;

import java.util.List;

import javax.persistence.Query;

import net.cloudengine.dao.jpa.ZoneRepository;
import net.cloudengine.model.geo.Zone;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class ZoneRepositoryImpl extends JPARepository<Zone, Long> implements ZoneRepository {

	public ZoneRepositoryImpl() {
		super(Zone.class);
	}

	@Override
	public List<Zone> getByType(String type) {
		Session s = this.getHibernateSession();
		Criteria c = s.createCriteria(Zone.class);
		c.add(Restrictions.eq("type", type));
		return cast(c.list(), Zone.class);
	}

	@Override
	public List<String> getZonesType() {
		Query query = getEntityManager().createNativeQuery(
			"SELECT distinct type " +
			"FROM tp_zone "
		);
		return cast(query.getResultList(), String.class);
	}
	
	public List<Zone> getByNameAndType(String name, String type) {
		Session s = this.getHibernateSession();
		Criteria c = s.createCriteria(Zone.class);
		c.add(Restrictions.eq("type", type));
		if (StringUtils.isNotEmpty(name)) {
			c.add(Restrictions.ilike("name", name, MatchMode.ANYWHERE));	
		}
		return cast(c.list(), Zone.class);	
	}
}
