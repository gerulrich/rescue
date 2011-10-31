package net.cloudengine.maps

import net.cloudengine.web.map.TileServer

class OSMTileServer extends TileServer {

	@Override
	public String tileUrl(int zoom, int x, int y) {
//		return "http://b.tile.openstreetmap.org/${zoom}/${x}/${y}.png";
		return "http://b.tah.openstreetmap.org/Tiles/tile/${zoom}/${x}/${y}.png";
	}

}
