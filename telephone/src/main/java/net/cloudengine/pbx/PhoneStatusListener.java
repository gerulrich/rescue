package net.cloudengine.pbx;

public interface PhoneStatusListener {
	
	/**
	 * Notifica el cambio de estado de una extension.
	 * @param extension
	 */
	void onStatusChange(String phoneStatus, Status newStatus);

}
