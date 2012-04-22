package net.cloudengine.cti.asterisk;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.cti.Connection;
import net.cloudengine.cti.ConnectionListener;
import net.cloudengine.rpc.controller.config.PropertyController;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.PingThread;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AsteriskConnection implements Connection {
	
	private PropertyController controller;
	private List<ManagerEventListener> listeners = new ArrayList<ManagerEventListener>();
	private List<ConnectionListener> connectionListeners = new ArrayList<ConnectionListener>();
	private DefaultAsteriskServer asteriskServer;
	private PingThread pingThread;
	private boolean connected;
	
	@Inject
	public AsteriskConnection(PropertyController controller) {
		this.controller = controller;
		this.connected = false;
		this.connect();
	}
	
	@Override
	public void connect() {
		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();
				String hostname = controller.getProperty("asterisk.hostname").getValue();
				String username = controller.getProperty("asterisk.manager.user").getValue();
				String password = controller.getProperty("asterisk.manager.pass").getValue();
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
					ManagerConnection c = asteriskServer.getManagerConnection();
					for (ManagerEventListener l : listeners) {
						c.addEventListener(l);
					}
					for(ConnectionListener l : connectionListeners) {
						l.onConnect();
					}
				}
			}
		};
		t.start();
	}
	
	@Override
	public void register(ManagerEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public AsteriskServer getAsteriskServer() {
		return asteriskServer;
	}

	@Override
	public void close() {
		if (connected) {
			pingThread.die();
			asteriskServer.getManagerConnection().logoff();
		}
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void register(ConnectionListener listener) {
		connectionListeners.add(listener);
	}
}
