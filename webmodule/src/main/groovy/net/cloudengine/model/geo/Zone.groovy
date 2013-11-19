package net.cloudengine.model.geo

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.NamedQuery

import net.cloudengine.model.map.geo.Feature

import org.hibernate.annotations.Type

import com.vividsolutions.jts.geom.Geometry;

@Entity
class Zone implements Feature {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;
	String name;
	String type;
	
	@Type(type='org.hibernatespatial.GeometryUserType')
	Geometry geom;

	@Override
	public Geometry getGeom() {
		return geom;
	}

	@Override
	public String getGeomType() {
		return 'Polygon';
	}

	@Override
	public Map<String, String> getProperties() {
		def map = ['id':'FX-11', 'name':'Radish', 'no':1234, 99:'Y'];
		return map;
	}
	
	
	
}
