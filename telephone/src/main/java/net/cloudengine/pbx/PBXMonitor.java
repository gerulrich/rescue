package net.cloudengine.pbx;
import java.util.Collection;


public interface PBXMonitor {
	
	/**
	 * Retorna los grupos de extensiones segun sus estados
	 * principales (conectadas, desconectadas, etc.)
	 * @return lista con los grupos de extensiones.
	 */
	Collection<Group> getGroups();
	
	/**
	 * Retorna todas las extensiones de la central telefonica.
	 * @return lista con las extensiones.
	 */
	Collection<PhoneExt> getAllPhoneExt();
	
	void addListener(PhoneStatusListener listener);
	
	Collection<CTIQueue> getQueues();

}
