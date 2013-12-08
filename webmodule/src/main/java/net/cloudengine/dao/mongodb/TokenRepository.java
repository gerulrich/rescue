package net.cloudengine.dao.mongodb;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.model.auth.RememberMeToken;

import org.bson.types.ObjectId;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public interface TokenRepository extends Repository<RememberMeToken, ObjectId>, PersistentTokenRepository {

}
