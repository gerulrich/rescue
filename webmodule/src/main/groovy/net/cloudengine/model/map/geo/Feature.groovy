package net.cloudengine.model.map.geo

import com.vividsolutions.jts.geom.Geometry

interface Feature {
	
	Geometry getGeom();
	String getGeomType();
	Map<String,String> getProperties();

}
