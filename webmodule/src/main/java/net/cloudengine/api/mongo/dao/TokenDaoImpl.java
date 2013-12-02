package net.cloudengine.api.mongo.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;

import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.auth.RememberMeToken;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

/**
 * Mongo based persistent login token repository implementation.
 *
 * @author German Ulrich
 */
public class TokenDaoImpl extends MongoStore<RememberMeToken, ObjectId> implements TokenDao {

	private static final Logger logger = LoggerFactory.getLogger(TokenDaoImpl.class);
	
	public TokenDaoImpl(MongoTemplate mongoTemplate) {
		super(mongoTemplate, RememberMeToken.class);
	}
	
	@Override
	public synchronized void createNewToken(PersistentRememberMeToken token) {
		if (logger.isDebugEnabled()) {
			logger.debug("Creando token de sesion para {}, usuario: {}", token.getSeries(), token.getUsername());
		}
		save(new RememberMeToken(token));
	}
	
	@Override
	public synchronized void updateToken(String series, String tokenValue, Date lastUsed) {
		RememberMeToken token = getRememberMeTokenBySeries(series);

		if (logger.isDebugEnabled()) {
			logger.debug("Actualizando token de sesion para {}, usuario: {}", token.getSeries(), token.getUsername());
		}
		
		token.setDate(lastUsed);
		token.setTokenValue(tokenValue);
		token.setSeries(series);
		update(token);
	}
	

	@Override
	public synchronized PersistentRememberMeToken getTokenForSeries(String seriesId) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Obteniendo token de sesion - seriesId={}", seriesId);
		}
		
		RememberMeToken token = getRememberMeTokenBySeries(seriesId);
		if (token != null)
			return token.toSpringToken();
		return null;
	}

	private RememberMeToken getRememberMeTokenBySeries(String seriesId) {
		RememberMeToken token = mongoTemplate.findOne(query(where("series").is(seriesId)), entityClass);
		return token;
	}
	
	private RememberMeToken getRememberMeTokenByUsername(String username) {
		RememberMeToken token = mongoTemplate.findOne(query(where("username").is(username)), entityClass);
		return token;
	}
	
	@Override
	public void removeUserTokens(String username) {

		if (logger.isDebugEnabled()) {
			logger.debug("Borrando token de sesion para usuario: "+username);
		}

		RememberMeToken token = getRememberMeTokenByUsername(username);
		if (token != null)
			delete(token.getId());
	}
	
	 

	
		
	
}