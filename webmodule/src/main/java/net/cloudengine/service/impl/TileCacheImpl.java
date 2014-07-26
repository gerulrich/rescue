package net.cloudengine.service.impl;

import net.cloudengine.dao.mongodb.TileRepository;
import net.cloudengine.model.geo.Tile;
import net.cloudengine.service.TileCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TileCacheImpl implements TileCache {

	private TileRepository tileRepository;
	
	@Autowired
	public TileCacheImpl(TileRepository tileRepository) {
		super();
		this.tileRepository = tileRepository;
	}

	@Override
	public Tile get(int zoom, int x, int y, String key) {
		return tileRepository.get(zoom, x, y, key);
	}

	@Override
	public void put(Tile tile) {
		tileRepository.save(tile);
	}

}
