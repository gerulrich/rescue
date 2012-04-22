package net.cloudengine.cti;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.manager.ManagerEventListener;

public interface Connection {
	
	AsteriskServer getAsteriskServer();
	void connect();
	boolean isConnected();
	void close();
	void register(ManagerEventListener listener);
	void register(ConnectionListener listener);

}
