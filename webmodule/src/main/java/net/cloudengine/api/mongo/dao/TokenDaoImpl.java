package net.cloudengine.api.mongo.dao;

import java.util.Date;

import net.cloudengine.api.Query;
import net.cloudengine.api.mongo.MongoDBWrapper;
import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.auth.RememberMeToken;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import com.google.code.morphia.Morphia;

/**
 * Mongo based persistent login token repository implementation.
 *
 * @author German Ulrich
 */
public class TokenDaoImpl extends MongoStore<RememberMeToken, ObjectId> implements TokenDao {

	private static final Logger logger = LoggerFactory.getLogger(TokenDaoImpl.class);
	
	public TokenDaoImpl(MongoDBWrapper wrapper, Morphia morphia) {
		super(wrapper, RememberMeToken.class, morphia);
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
		Query<RememberMeToken> query = createQuery().field("series").eq(series);
		RememberMeToken token = query.get();

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
		
		Query<RememberMeToken> query = createQuery().field("series").eq(seriesId);
		RememberMeToken token = query.get();
		if (token != null)
			return token.toSpringToken();
		return null;
	}
	
	@Override
	public void removeUserTokens(String username) {

		if (logger.isDebugEnabled()) {
			logger.debug("Borrando token de sesion para usuario: "+username);
		}

		Query<RememberMeToken> query = createQuery().field("username").eq(username);
		RememberMeToken token = query.get();
		if (token != null)
			delete(token.getId());
	}

	
		
	
}