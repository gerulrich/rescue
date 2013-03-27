package net.cloudengine.service.auth;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.User;
import net.cloudengine.util.Cipher;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.codec.Base64;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private UserService service;
	
	private PersistentTokenRepository tokenRepository;
	private Datastore<MongoRememberMeToken, ObjectId> datastore;
	
	public static final int DEFAULT_SERIES_LENGTH = 16;
    public static final int DEFAULT_TOKEN_LENGTH = 16;
    private static final String DELIMITER = ":";    

    private int seriesLength = DEFAULT_SERIES_LENGTH;
    private int tokenLength = DEFAULT_TOKEN_LENGTH;
    
	private SecureRandom random;
	
	
	public AuthenticationServiceImpl() {
		super();
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (Exception e) {e.printStackTrace(); }
	}
	
	
	@Autowired
	public void setDatastore(@Qualifier("tokenStore") Datastore<MongoRememberMeToken, ObjectId> datastore) {
		this.datastore = datastore;
	}

	@Autowired
	public void setService(UserService service) {
		this.service = service;
	}
	
	@Autowired
	public void setTokenRepository(PersistentTokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Override
	public String login(String username, String password) {
		User user = service.getByUsername(username);
		if (user != null && password != null) {
			boolean aut = new Cipher().encrypt(password).equals(user.getPassword());
			if (!aut) {
				return null;
			} else {
				List<Permission> permissions = service.getPermissionForUser(user);
				user.setPermissions(permissions);
				if (user.hasPermission("DESKTOP")) {
					UsernamePasswordAuthenticationToken ut = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
					SecurityContext sctx = SecurityContextHolder.createEmptyContext(); 
					sctx.setAuthentication(ut);
					
					ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
					HttpSession session = attr.getRequest().getSession(true);
					String sessionId = session.getId();
					session.setAttribute("SPRING_SECURITY_CONTEXT", sctx);
					
					return sessionId;
				} else {
					throw new RuntimeException("No posee los permisos necesario para acceder a la aplicación.");
				}			
			}
		}
		return null;
	}

	@Override
	public String getAuthToken(String username, String password) {
		if (login(username, password) != null) {
			MongoRememberMeToken persistenttoken = datastore.createQuery().field("username").eq(username).get();
			if (persistenttoken != null) {
				return encodeCookie(new String[] {persistenttoken.getSeries(), persistenttoken.getTokenValue()});
			} else {
				PersistentRememberMeToken token = new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData(), new Date());
				tokenRepository.createNewToken(token);
				return encodeCookie(new String[] {token.getSeries(), token.getTokenValue()});
			}
		}
		return null;
	}
	
    /**
     * Inverse operation of decodeCookie.
     *
     * @param cookieTokens the tokens to be encoded.
     * @return base64 encoding of the tokens concatenated with the ":" delimiter.
     */
    protected String encodeCookie(String[] cookieTokens) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < cookieTokens.length; i++) {
            sb.append(cookieTokens[i]);

            if (i < cookieTokens.length - 1) {
                sb.append(DELIMITER);
            }
        }

        String value = sb.toString();

        sb = new StringBuilder(new String(Base64.encode(value.getBytes())));

        while (sb.charAt(sb.length() - 1) == '=') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
    
    protected String generateSeriesData() {
        byte[] newSeries = new byte[seriesLength];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }

    protected String generateTokenData() {
        byte[] newToken = new byte[tokenLength];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }    
	

}
