package net.shapefile.geometry;

import java.util.Arrays;
import java.util.List;

import net.shapefile.Point;
import net.shapefile.ShapeObject;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;



public class WKTUtils {
	
	public static Geometry toGeometry(ShapeObject shape) {
		switch (shape.getType()) {
//			case 1: return formatPoint(shape);
			case 3: return formatPolyline(shape);
			case 5: return formatPolygon(shape);
//			case 8: return formatMultipoint(shape);
			default: throw new IllegalArgumentException("Tipo no implementado por el momento");
		}
	}
	
	public static String format(ShapeObject shape) {
		
		switch (shape.getType()) {
			case 1: return formatPoint(shape);
			case 3: return formatPolylineAsText(shape);
			case 5: return formatPolygonString(shape);
			case 8: return formatMultipoint(shape);
			default: throw new IllegalArgumentException("Tipo no implementado por el momento");
		}
	}
	
	private static String formatPolylineAsText(ShapeObject shape) {
		
		GeometryFactory factory = new GeometryFactory();
		if (shape.getPartCount() == 1) {
			LineString lineString = factory.createLineString(points2Coords(shape.getPoints()));
			return new WKTWriter().write(lineString);
		} else {
			LineString lines[] = new LineString[shape.getPartCount()];
			for (int i = 0; i < lines.length; i++) {
				lines[i] = factory.createLineString(points2Coords(shape.getPoints(i+1)));
			}
			MultiLineString multi = factory.createMultiLineString(lines);
			return new WKTWriter().write(multi);
		}
	}
	
	private static String formatPoint(ShapeObject shape) {
		Point point = shape.getPoint(0);
		return WKTWriter.toPoint(new Coordinate(point.getX(), point.getY()));
	}
	
	private static String formatMultipoint(ShapeObject shape) {
		GeometryFactory factory = new GeometryFactory();
		Coordinate coords[] = points2Coords(shape.getPoints());
		Geometry geom = factory.createMultiPoint(coords);
		return new WKTWriter().write(geom);
	}
	
	private static String formatPolygonString(ShapeObject shape) {
		
		GeometryFactory factory = new GeometryFactory();
		LinearRing rings[] = new LinearRing[shape.getPartCount()];
		for (int i = 0; i < rings.length; i++) {
			rings[i] = factory.createLinearRing(points2Coords(shape.getPoints(i+1)));
		}
		Polygon polygon = null;
		if (rings.length > 1)
			polygon = factory.createPolygon(rings[0], Arrays.copyOfRange(rings, 1, rings.length-1));
		else
			polygon = factory.createPolygon(rings[0], new LinearRing[0]);
		return new WKTWriter().write(polygon);
	}

	private static Coordinate[] points2Coords(List<Point> points) {
		Coordinate coords[] = new Coordinate[points.size()];
		for (int i = 0; i < coords.length; i++) {
			Point point = points.get(i);
			coords[i] = new Coordinate(point.getX(), point.getY());
		}
		return coords;
	}
	
	/**
	 * Transforma un {@link ShapeObject} en un {@link Geometry}
	 * @param shape a transformar
	 * @return una instancia de tipo {@link Geometry}
	 */
	private static Geometry formatPolyline(ShapeObject shape) {
		
		GeometryFactory factory = new GeometryFactory();
		if (shape.getPartCount() == 1) {
			LineString lineString = factory.createLineString(points2Coords(shape.getPoints()));
			return lineString;
		} else {
			LineString lines[] = new LineString[shape.getPartCount()];
			for (int i = 0; i < lines.length; i++) {
				lines[i] = factory.createLineString(points2Coords(shape.getPoints(i+1)));
			}
			MultiLineString multi = factory.createMultiLineString(lines);
			return multi;
		}
	}
	
	private static Geometry formatPolygon(ShapeObject shape) {
		
		GeometryFactory factory = new GeometryFactory();
		LinearRing rings[] = new LinearRing[shape.getPartCount()];
		for (int i = 0; i < rings.length; i++) {
			rings[i] = factory.createLinearRing(points2Coords(shape.getPoints(i+1)));
		}
		Polygon polygon = null;
		if (rings.length > 1)
			polygon = factory.createPolygon(rings[0], Arrays.copyOfRange(rings, 1, rings.length-1));
		else
			polygon = factory.createPolygon(rings[0], new LinearRing[0]);
		return polygon;
	}	

}
