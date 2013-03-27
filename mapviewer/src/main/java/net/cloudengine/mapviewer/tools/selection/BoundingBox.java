package net.cloudengine.mapviewer.tools.selection;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class BoundingBox {
	double north;
	double south;
	double east;
	double west;
	
	Envelope envelope;
	
	public BoundingBox() {
		super();
	}
	
	public BoundingBox(double west, double north, double east, double south) {
		super();
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
		envelope = new Envelope(new Coordinate(west, north), new Coordinate(east, south));
	}
	
	public boolean contains(double x, double y) {
		return envelope.contains(x, y);
	}
	
	public double getNorth() {
		return north;
	}
	public void setNorth(double north) {
		this.north = north;
	}
	public double getSouth() {
		return south;
	}
	public void setSouth(double south) {
		this.south = south;
	}
	public double getEast() {
		return east;
	}
	public void setEast(double east) {
		this.east = east;
	}
	public double getWest() {
		return west;
	}
	public void setWest(double west) {
		this.west = west;
	}
	public Envelope getEnvelope() {
		return envelope;
	}
}