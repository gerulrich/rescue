package net.cloudengine.service.auth;

/**
 * Interface que establece las acciones posibles
 * para la integración de usuarios con openfire.
 * @author German
 *
 */
public interface XMPPService {
	
	/**
	 * Permite agregar un usuario al servidor xmpp
	 * @param email email del usaurio a agregar
	 * @param password password del usaurio
	 * @param name nombre descriptivo del usaurio
	 * @return
	 */
	public int addUser(String email, String password, String name);
	
	/**
	 * Permite modificar un usuario del servidor xmpp
	 * @param email email del usaurio a agregar
	 * @param password password del usaurio
	 * @param name nombre descriptivo del usaurio
	 * @return
	 */
	public int updateUser(String email, String password, String name);
	
	/**
	 * Permite modificar un usuario del servidor xmpp
	 * @param email del usaurio que se quiere eliminar.
	 * @return
	 */
	public int deleteUser(String email);

}
