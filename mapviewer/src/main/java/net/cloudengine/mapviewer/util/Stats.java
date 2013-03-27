package net.cloudengine.mapviewer.util;

public class Stats {
	public int tileCount;
	public long dt;

	public Stats() {
		reset();
	}

	public void reset() {
		tileCount = 0;
		dt = 0;
	}

	public void incTileCount() {
		tileCount++;
	}
	
	public int getTileCount() {
		return tileCount;
	}

	public void setTileCount(int tileCount) {
		this.tileCount = tileCount;
	}

	public long getDt() {
		return dt;
	}

	public void setDt(long dt) {
		this.dt = dt;
	}
	
	
}