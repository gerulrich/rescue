package net.cloudengine.maps

import net.cloudengine.web.map.AbstractTileServer;
import net.cloudengine.web.map.TileServer


class OSMTileServer extends AbstractTileServer {

	@Override
	public String getUrlTile(int zoom, int x, int y) {
		return "http://b.tah.openstreetmap.org/Tiles/tile/${zoom}/${x}/${y}.png";
	}
	
	@Override
	public String getKey() {
		return "osm";
	}
}
