package net.cloudengine.service.auth;

import java.util.Date;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Mongo based persistent login token repository implementation.
 *
 * @author German Ulrich
 */
public class MongoTokenRepositoryImpl implements PersistentTokenRepository {

	private static final Logger logger = LoggerFactory.getLogger(MongoTokenRepositoryImpl.class);
	
	Datastore<MongoRememberMeToken, ObjectId> datastore;
	
	public MongoTokenRepositoryImpl(Datastore<MongoRememberMeToken, ObjectId> datastore) {
		this.datastore = datastore;
	}
	
	
	@Override
	public synchronized void createNewToken(PersistentRememberMeToken token) {

		if (logger.isDebugEnabled()) {
			logger.debug("Creando token de sesion para {}, usuario: {}", token.getSeries(), token.getUsername());
		}

		datastore.save(new MongoRememberMeToken(token));
	}

	@Override
	public synchronized void updateToken(String series, String tokenValue, Date lastUsed) {

		Query<MongoRememberMeToken> query = datastore.createQuery().field("series").eq(series);
		MongoRememberMeToken token = query.get();

		if (logger.isDebugEnabled()) {
			logger.debug("Actualizando token de sesion para {}, usuario: {}", token.getSeries(), token.getUsername());
		}

		token.setDate(lastUsed);
		token.setTokenValue(tokenValue);
		token.setSeries(series);
		datastore.update(token);
	}

	@Override
	public synchronized PersistentRememberMeToken getTokenForSeries(String seriesId) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Obteniendo token de sesion - seriesId={}", seriesId);
		}
		
		Query<MongoRememberMeToken> query = datastore.createQuery().field("series").eq(seriesId);
		MongoRememberMeToken token = query.get();
		if (token != null)
			return token.toSpringToken();
		return null;
	}

	@Override
	public void removeUserTokens(String username) {

		if (logger.isDebugEnabled()) {
			logger.debug("Borrando token de sesion para usuario: "+username);
		}

		Query<MongoRememberMeToken> query = datastore.createQuery().field("username").eq(username);
		MongoRememberMeToken token = query.get();
		if (token != null)
			datastore.delete(token.getId());
	}
}