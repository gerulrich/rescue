package net.cloudengine.service.map.impl;

import javax.annotation.Resource;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.geo.Tile;
import net.cloudengine.service.map.TileCache;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class TileCacheImpl implements TileCache {

	@Resource(name="tileStore")
	private Datastore<Tile, ObjectId> ds = null;
	
	public void setDs(Datastore<Tile, ObjectId> ds) {
		this.ds = ds;
	}

	@Override
	public Tile get(int zoom, int x, int y, String key) {
		Query<Tile> query = ds.createQuery()
			.field("x").eq(Integer.valueOf(x))
			.field("y").eq(Integer.valueOf(y))
			.field("z").eq(Integer.valueOf(zoom))
			.field("key").eq(key);
		return query.get();
	}

	@Override
	public void put(Tile tile) {
		ds.save(tile);
	}

}
