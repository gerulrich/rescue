package net.cloudengine.model.map.geo

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.Polygon

class GeoFeature {
	
	String type = "Feature";
	GeoData geometry;
	String name;
//	Map<String,String> properties;
	
	GeoFeature(Feature feature) {
		name = feature.getName();
		Geometry g = feature.getGeom();

		Object coordinates;
		if ("Polygon".equals(feature.getGeomType())) {
			Polygon p = (Polygon)feature.getGeom();
			Coordinate[] coords = p.getExteriorRing().getCoordinates();
			int holes = p.getNumInteriorRing();
			coordinates = new double[1+holes][][];
			coordinates[0] = new double[coords.length+1][2];
			
			coords.eachWithIndex { c, idx ->
				coordinates[0][idx][0] = c.x;
				coordinates[0][idx][1] = c.y;
			}
			coordinates[0][coords.length][0] = coords[0].x;
			coordinates[0][coords.length][1] = coords[0].y;
			
			for(int i = 0; i < holes; i++) {
				Coordinate[] coordsHole = p.getInteriorRingN(i).getCoordinates();
				coordinates[i+1] = new double[coordsHole.length+1][2]
				coordsHole.eachWithIndex { c, idx ->
					coordinates[i+1][idx][0] = c.x;
					coordinates[i+1][idx][1] = c.y;
				}
				coordinates[i+1][coordsHole.length][0] = coordsHole[0].x;
				coordinates[i+1][coordsHole.length][1] = coordsHole[0].y;
			}
			
			geometry = new GeoData(feature.getGeomType(), coordinates);
		} else {
			throw new IllegalArgumentException("Tipo de geometria no implementado");
		}
	}
}
