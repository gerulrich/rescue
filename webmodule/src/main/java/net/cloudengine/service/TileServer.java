package net.cloudengine.service;

import net.cloudengine.model.geo.Tile;

public interface TileServer {
	
	
	Tile get(int zoom, int x, int y);
	String getKey();
	
	

}