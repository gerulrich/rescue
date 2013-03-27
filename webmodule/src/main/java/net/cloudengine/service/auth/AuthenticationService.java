package net.cloudengine.service.auth;

public interface AuthenticationService {
	
	/**
	 * Verifica las credenciales de usuario.
	 * @param username
	 * @param password
	 * @return
	 */
	String login(String username, String password);
	
	/**
	 * Retorna un token de autenticación de usuario si 
	 * las credenciales de usuario son válidas.
	 * @param username
	 * @param password
	 * @return
	 */
	String getAuthToken(String username, String password);


}
