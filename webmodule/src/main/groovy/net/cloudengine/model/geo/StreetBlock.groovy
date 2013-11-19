package net.cloudengine.model.geo


import javax.persistence.Column
import javax.persistence.Entity;
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery

import net.shapefile.Point

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.linearref.LengthIndexedLine

@Entity
class StreetBlock {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;
	@Column(columnDefinition="TEXT")
	String name;
	String type;
	
	int fromLeft;
	int toLeft;
	
	int fromRight;
	int toRight;
	int vstart;
	int vend;
	
	@Type(type='org.hibernatespatial.GeometryUserType')
	Geometry geom;
	
	public Point pointForNumber(int nro) {
		double min = Math.min(fromLeft, fromRight);
		double max = Math.max(toLeft, toRight);
		double porcentage = 0D;
		if ( min != max ) {
			porcentage = (nro-min)/(max-min);
		}
		LengthIndexedLine indexedLine = new LengthIndexedLine(geom);
		double length = geom.getLength();
		Coordinate c = indexedLine.extractPoint(porcentage * length)
		return new Point(c.x, c.y);
	}
}
