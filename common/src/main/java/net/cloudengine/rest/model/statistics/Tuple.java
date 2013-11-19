package net.cloudengine.rest.model.statistics;

public class Tuple<X,Y> {
	
	private X x;
	private Y y;
	
	public Tuple(X x, Y y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public X getX() {
		return x;
	}
	
	public void setX(X x) {
		this.x = x;
	}
	
	public Y getY() {
		return y;
	}
	
	public void setY(Y y) {
		this.y = y;
	}
}
