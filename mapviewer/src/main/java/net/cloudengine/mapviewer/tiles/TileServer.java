package net.cloudengine.mapviewer.tiles;


public class TileServer {
	
	private final String url;
	private final int maxZoom;
	private boolean broken;
	private String name;
	private TileServerType type;

	public TileServer(String name, String url, int maxZoom, TileServerType type) {
		this.name = name;
		this.url = url;
		this.maxZoom = maxZoom;
		this.type = type;
	}

	public String toString() {
		return url;
	}
	
	public String getName() {
		return name;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public String getURL() {
		return url;
	}

	public boolean isBroken() {
		return broken;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public TileServerType getType() {
		return type;
	}

	public void setType(TileServerType type) {
		this.type = type;
	}
}
