package net.cloudengine.dao.mongodb;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.model.geo.Tile;

import org.bson.types.ObjectId;

public interface TileRepository extends Repository<Tile, ObjectId> {
	
	Tile get(int zoom, int x, int y, String key);

}
