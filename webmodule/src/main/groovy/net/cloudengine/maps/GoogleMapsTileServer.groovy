package net.cloudengine.maps

import net.cloudengine.web.map.TileServer;

class GoogleMapsTileServer extends TileServer {

	@Override
	public String tileUrl(int zoom, int x, int y) {
		return "http://mt1.google.com/vt/lyrs=m@139&hl=es&x=${x}&y=${y}&z=${zoom}&s=Galil";
//		return "http://khm0.google.com.ar/kh/v=74&x=${x}&y=${y}&z=${zoom}&s=Galil";
	}
}
