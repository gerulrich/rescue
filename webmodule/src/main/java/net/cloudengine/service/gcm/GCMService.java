package net.cloudengine.service.gcm;

import net.cloudengine.model.auth.User;
import net.cloudengine.model.gcm.RegisteredDevice;

public interface GCMService {
	
	/**
	 * Envia un mensaje a todos los dispositivos del usuario.
	 * @param msg mensaje a enviar
	 * @param toUser
	 */
	void sendMessage(GCMMessage msg, User toUser);
	
	/**
	 * Retirna todos los dispositivos registrados del usuario.
	 * @param user
	 * @return
	 */
	RegisteredDevice getDevices(User user);	
	
	/**
	 * 
	 * @param user
	 * @param deviceId
	 */
	void registerDevice(String user, String deviceId);

}
