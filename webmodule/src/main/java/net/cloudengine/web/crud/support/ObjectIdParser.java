package net.cloudengine.web.crud.support;

import org.bson.types.ObjectId;

public class ObjectIdParser implements IdParser<ObjectId> {

	@Override
	public ObjectId parse(String text) {
		return new ObjectId(text);
	}

}
