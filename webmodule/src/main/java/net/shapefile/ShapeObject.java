package net.shapefile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.dbf.Record;

public class ShapeObject {
	
	public static final int UNDEFINED = 0;
	public static final int POINT = 1;
	public static final int POLYLINE = 3;
	public static final int POLYGON = 5;
	public static final int MULTIPOINT = 8;

	private List<Point> points = new ArrayList<Point>();
	private List<Integer> parts = new ArrayList<Integer>();
	private Record record;
	private BoundingBox boundingBox = new BoundingBox();
	private int type;

	public ShapeObject() {
		this.type = 0;
		this.parts.add(0);
	}

	public ShapeObject(int type) {
		this.type = type;
		if (this.type != 1)
			this.parts.add(0);
	}

	public void addPoint(Point point) {
		this.points.add(point);
	}

	public void removePoint(int index) {
		this.points.remove(index);
	}

	public Point getPoint(int index) {
		return (Point) this.points.get(index);
	}

	public List<Point> getPoints() {
		return this.points;
	}

	public void setPoints(Collection<Point> paramCollection) {
		this.points = new ArrayList<Point>(paramCollection);
	}

	public void addPart(int paramInt) {
		if (paramInt != 0)
			this.parts.add(Integer.valueOf(paramInt));
	}

	public void removePart(int paramInt) {
		this.parts.remove(paramInt);
	}

	public int getPart(int paramInt) {
		Integer localInteger = (Integer) this.parts.get(paramInt);
		return localInteger.intValue();
	}

	public void setParts(Collection<Integer> paramCollection) {
		this.parts = new ArrayList<Integer>(paramCollection);
	}

	public List<Integer> getParts() {
		return this.parts;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int paramInt) {
		this.type = paramInt;
	}

	public Record getRecord() {
		return this.record;
	}

	public void setRecord(Record paramRecord) {
		this.record = paramRecord;
	}

	public void setBoundingBox(BoundingBox paramBoundingBox) {
		this.boundingBox = paramBoundingBox;
	}

	public BoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	public int getPointCount() {
		return this.points.size();
	}

	public int getPartCount() {
		return this.parts.size();
	}
	
	public List<Point> getPoints(int part) {
		if (part > this.getPartCount()) {
			throw new IllegalArgumentException("");//FIXME
		}
		
		if (part == this.getPartCount()) {
			int partIndexFrom = this.getPart(part-1);
			return this.getPoints().subList(partIndexFrom, this.getPointCount());
		} else {
			int partIndexFrom = this.getPart(part-1);
			int partIndexTo = this.getPart(part);
			return this.getPoints().subList(partIndexFrom, partIndexTo);			
		}
	}

	public void computeExtents() {
		switch (this.type) {
		case 1:
			break;
		case 0:
		case 3:
		case 5:
		case 8:
			if (getPointCount() > 0) {
				Point localPoint1 = getPoint(0);
				this.boundingBox.setXMin(localPoint1.getX());
				this.boundingBox.setXMax(localPoint1.getX());
				this.boundingBox.setYMin(localPoint1.getY());
				this.boundingBox.setYMax(localPoint1.getY());
			}
			for (int i = 0; i < getPoints().size(); i++) {
				Point localPoint2 = getPoint(i);
				if (this.boundingBox.getXMin() > localPoint2.getX())
					this.boundingBox.setXMin(localPoint2.getX());
				if (this.boundingBox.getXMax() < localPoint2.getX())
					this.boundingBox.setXMax(localPoint2.getX());
				if (this.boundingBox.getYMin() > localPoint2.getY())
					this.boundingBox.setYMin(localPoint2.getY());
				if (this.boundingBox.getYMax() >= localPoint2.getY())
					continue;
				this.boundingBox.setYMax(localPoint2.getY());
			}
		case 2:
		case 4:
		case 6:
		case 7:
		}
	}
}