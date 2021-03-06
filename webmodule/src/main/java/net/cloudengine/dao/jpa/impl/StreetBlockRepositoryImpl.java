package net.cloudengine.dao.jpa.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import net.cloudengine.dao.jpa.StreetBlockRepository;
import net.cloudengine.model.geo.StreetBlock;
import net.cloudengine.model.geo.StreetIntersection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StreetBlockRepositoryImpl extends JPARepository<StreetBlock, Long> implements StreetBlockRepository {

	public StreetBlockRepositoryImpl() {
		super(StreetBlock.class);
	}

	@Override
	@Transactional
	public Collection<StreetBlock> find(String loc, Integer nro) {
		Collection<StreetBlock> street = null;
		
		try {
			Query query = getEntityManager().createNativeQuery(
					"SELECT id, name, type, from_left, to_left, from_right, to_right, " +
					"vstart, vend, geom "+
					"FROM tp_street_block " +
					"WHERE name like ? AND "+
					"( ( ? between from_left and  to_left ) OR ( ? between from_right and  to_right ) ) ",
					StreetBlock.class
			);
			query.setParameter(1, "%"+loc+"%");
			query.setParameter(2, nro);
			query.setParameter(3, nro);
			street = cast(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return street;
	}

	@Override
	@Transactional
	public StreetIntersection find(String loc1, String loc2) {

		StreetIntersection intersection = null;
		
		try {
			Query query = getEntityManager().createNativeQuery(
					"SELECT s1.name AS name1, s1.geom AS geom1," +
					"s2.name AS name2, s2.geom AS geom2 " +
					"FROM tp_street_block AS s1 " +
					"INNER JOIN tp_street_block AS s2 ON (s1.vstart = s2.vstart OR s1.vend = s2.vend OR s1.vstart = s2.vend) " +
					"WHERE s1.name like ? and s1.name <> s2.name and s2.name like ?",
					StreetIntersection.class
			);
			query.setParameter(1, "%"+loc1+"%");
			query.setParameter(2, "%"+loc1+"%");
			
			query.setMaxResults(1);
			intersection  = (StreetIntersection) query.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intersection;
	}
	
	@Override
	@Transactional
	public StreetBlock findStreetBlockByStartVertix(int startVertix) {
		Criteria c = this.getHibernateSession().createCriteria(StreetBlock.class);
		c.add(Restrictions.eq("vstart", startVertix));
		List<StreetBlock> list = cast(c.list());
		if (list.isEmpty()) {
			return null;
		}
		if (list.size() > 0) {
			int random = (int)(Math.random()*100);
			return list.get(random%list.size());
		} else {
			return list.get(0);
		}
	}
	
}
