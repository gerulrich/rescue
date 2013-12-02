package net.cloudengine.service.auth;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.auth.AuthenticationToken;
import net.cloudengine.model.auth.User;
import net.cloudengine.service.web.SessionService;
import net.cloudengine.util.Cipher;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private UserService userService;
	private SessionService sessionService;
	private Datastore<AuthenticationToken, ObjectId> authTokenStore;
	private SecureRandom random;
	
	@Autowired
	public AuthenticationServiceImpl(UserService userService, SessionService sessionService,
			@Qualifier("authTokenStore") Datastore<AuthenticationToken, ObjectId> authTokenStore) throws NoSuchAlgorithmException {
		super();
		this.userService = userService;
		this.sessionService = sessionService;
		this.authTokenStore = authTokenStore;
		random = SecureRandom.getInstance("SHA1PRNG");
	}
	
	@Override
	public String login(String username, String password) {
		User user = userService.getByUsername(username);
		if (user != null && password != null) {
			boolean aut = new Cipher().encrypt(password).equals(user.getPassword());
			if (!aut) {
				return null;
			} else {
				if (user.hasPermission("DESKTOP") && user.getGroup() != null) {
					return sessionService.getSessionId(user);
				} else {
					throw new RuntimeException("No posee los permisos necesario para acceder a la aplicaci\u00F3n.");
				}
			}
		}
		return null;
	}
	
	@Override
	public String createToken(String username, String password) {
		User user = userService.getByUsername(username);
		String token = null;
		if (user != null) {
			if (isValidUser(user, password)) {
				AuthenticationToken authToken = authTokenStore.findOne("username", username);
				if (authToken == null) {
					authToken = createToken(username);
					authTokenStore.save(authToken);
				}
				token = encodeTokens(new String[] {authToken.getSeries(), authToken.getTokenValue()});
			}
		}
		return token;
	}

	@Override
	public User getUserByToken(String token) {
		String values[] = decodeToken(token);
		if (values != null && values.length == 2) {
			AuthenticationToken authToken = authTokenStore.findOne("series", values[0]);
			if (authToken != null && values[1].equals(authToken.getTokenValue())) {
				return userService.getByUsername(authToken.getUsername());
			}
		}
		return null;
	}
	
	private AuthenticationToken createToken(String username) {
		return new AuthenticationToken(username, generateSeriesData(), generateTokenData(), new Date());
	}
	
	private boolean isValidUser(User user, String password) {
		if (!user.getPassword().equals(new Cipher().encrypt(password))) {
			return false;
		}
		return user.hasPermission("DESKTOP") && user.getGroup() != null;
	}
	
	protected String generateSeriesData() {
        byte[] newSeries = new byte[16];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }
	
    protected String generateTokenData() {
        byte[] newToken = new byte[16];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }    
    
    /**
     * Decodes the cookie and splits it into a set of token strings using the ":" delimiter.
     *
     * @param cookieValue the value obtained from the submitted cookie
     * @return the array of tokens.
     * @throws InvalidCookieException if the cookie was not base64 encoded.
     */
    protected String[] decodeToken(String tokenValue) throws InvalidCookieException {
        for (int j = 0; j < tokenValue.length() % 4; j++) {
        	tokenValue = tokenValue + "=";
        }

        if (!Base64.isBase64(tokenValue.getBytes())) {
            return null;
        }

        String valueAsPlainText = new String(Base64.decode(tokenValue.getBytes()));

        return valueAsPlainText.split(":");
    }
    
    /**
     * Inverse operation of decodeTokens.
     *
     * @param tokens the tokens to be encoded.
     * @return base64 encoding of the tokens concatenated with the ":" delimiter.
     */
    protected String encodeTokens(String[] tokens) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < tokens.length; i++) {
            sb.append(tokens[i]);

            if (i < tokens.length - 1) {
                sb.append(":");
            }
        }

        String value = sb.toString();

        sb = new StringBuilder(new String(Base64.encode(value.getBytes())));

        while (sb.charAt(sb.length() - 1) == '=') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }    
}
