package net.cloudengine.api.jpa.dao;

import javax.persistence.Query;

import net.cloudengine.api.jpa.JPADatastore;
import net.cloudengine.model.map.Street;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StreetDaoImpl extends JPADatastore<Street, Long> implements StreetDao {

	public StreetDaoImpl() {
		super(Street.class);
	}

	@Override
	@Transactional
	public Street find(String loc, Integer nro) {
		Query query = this.entityManager.createNativeQuery("findStreetByNameAndNumber");
		query.setParameter(1, loc);
		query.setParameter(2, nro);
		query.setParameter(3, nro);
		
		query.setMaxResults(1);
		return (Street) query.getSingleResult();
	}

	
}
