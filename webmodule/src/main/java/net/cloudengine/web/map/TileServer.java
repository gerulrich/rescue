package net.cloudengine.web.map;

public abstract class TileServer {
	
	
	public byte[] get(int zoom, int x, int y) {
		return null;
	}
	
	protected abstract String tileUrl(int zoom, int x, int y);	

}