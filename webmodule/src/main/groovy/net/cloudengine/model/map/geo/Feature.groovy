package net.cloudengine.model.map.geo

import com.vividsolutions.jts.geom.Geometry

interface Feature {
	
	Geometry getGeom();
	String getGeomType();
	String getName();
	Map<String,String> getProperties();

}
