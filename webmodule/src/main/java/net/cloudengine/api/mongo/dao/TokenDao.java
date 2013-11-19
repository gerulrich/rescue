package net.cloudengine.api.mongo.dao;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.RememberMeToken;

import org.bson.types.ObjectId;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public interface TokenDao extends Datastore<RememberMeToken, ObjectId>, PersistentTokenRepository {

}
