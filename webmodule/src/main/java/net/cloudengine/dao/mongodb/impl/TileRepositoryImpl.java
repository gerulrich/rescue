package net.cloudengine.dao.mongodb.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import net.cloudengine.dao.mongodb.TileRepository;
import net.cloudengine.model.geo.Tile;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TileRepositoryImpl extends MongoRepository<Tile, ObjectId> implements TileRepository {

	public TileRepositoryImpl(MongoTemplate mongoTemplate) {
		super(Tile.class, mongoTemplate);
	}

	@Override
	public Tile get(int zoom, int x, int y, String key) {
		return mongoTemplate.findOne(query(
				where("x").is(x)
				.and("y").is(y)
				.and("z").is(zoom)
				.and("_key").is(key)), this.getType());
	}

}
