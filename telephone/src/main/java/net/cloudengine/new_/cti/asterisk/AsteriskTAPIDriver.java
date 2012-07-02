package net.cloudengine.new_.cti.asterisk;

import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.TAPIDriver;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.manager.PingThread;

public class AsteriskTAPIDriver implements TAPIDriver {

	private AsteriskEventProvider eventProvider;
	private AsteriskServer asteriskServer;
	private PingThread pingThread;
	private boolean connected = false;
	
	public AsteriskTAPIDriver() {
		this.eventProvider = new AsteriskEventProvider();
		startConnection();
	}
	
	@Override
	public EventProvider createEventProvider() {
		return eventProvider;
	}
	
	@Override
	public boolean isConnected() {
		return connected;
	}
	
	private void startConnection() {
		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();
				String hostname = "192.168.0.103";
				String username = "manager";
				String password = "manager";
				asteriskServer = new DefaultAsteriskServer(hostname, username, password);
				try {
					asteriskServer.initialize();
					pingThread = new PingThread(asteriskServer.getManagerConnection());
					pingThread.start();
					connected = true;
				} catch (Exception e) {
					connected = false;
				}
				
				if (connected) {
					eventProvider.connectionEstablished(asteriskServer.getManagerConnection());
				}
			}
		};
		t.start();
	}

}
