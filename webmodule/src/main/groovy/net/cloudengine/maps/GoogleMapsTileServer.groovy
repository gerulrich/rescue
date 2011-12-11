package net.cloudengine.maps

import net.cloudengine.web.map.AbstractTileServer;
import net.cloudengine.web.map.TileServer;

class GoogleMapsTileServer extends AbstractTileServer {

	@Override
	public String getUrlTile(int zoom, int x, int y) {
		return "http://mt1.google.com/vt/lyrs=m@139&hl=es&x=${x}&y=${y}&z=${zoom}&s=Galil";
	}
	
	@Override
	public String getKey() {
		return "googlemaps";
	}

}
