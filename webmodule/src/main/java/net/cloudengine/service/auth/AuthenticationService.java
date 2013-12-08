package net.cloudengine.service.auth;

import net.cloudengine.model.auth.User;

public interface AuthenticationService {
	
	/**
	 * Verifica las credenciales de usuario.
	 * @param username
	 * @param password
	 * @return
	 */
	String login(String username, String password);
	
	/**
	 * Crea un token se sesion.
	 * @param username
	 * @param password
	 * @return
	 */
	String createToken(String username, String password);
	
	/**
	 * Obtiene el usuario a partir de un token.
	 * @param token
	 * @return
	 */
	User getUserByToken(String token);

}
