package net.shapefile;

public class BoundingBox {
	
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;

	public BoundingBox() {
	}

	public BoundingBox(double xMin, double yMin, double xMax, double yMax) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}

	public double getXMin() {
		return xMin;
	}

	public double getXMax() {
		return xMax;
	}

	public double getYMin() {
		return yMin;
	}

	public double getYMax() {
		return yMax;
	}

	public void setXMin(double paramDouble) {
		xMin = paramDouble;
	}

	public void setXMax(double paramDouble) {
		xMax = paramDouble;
	}

	public void setYMin(double paramDouble) {
		yMin = paramDouble;
	}

	public void setYMax(double paramDouble) {
		yMax = paramDouble;
	}
}