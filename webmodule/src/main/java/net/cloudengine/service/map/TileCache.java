package net.cloudengine.service.map;

import net.cloudengine.model.geo.Tile;

public interface TileCache {
	
	Tile get(int zoom, int x, int y, String key);
	
	void put(Tile tile);

}
