package net.cloudengine.new_.cti.asterisk;

import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.TAPIDriver;

import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.PingThread;
import org.asteriskjava.manager.TimeoutException;

public class AsteriskTAPIDriver implements TAPIDriver {

	private AsteriskEventProvider eventProvider;
	private ManagerConnection connection;
	private PingThread pingThread;
	private boolean connected = false;
	private boolean shutdown = false;
	
	private String host;
	private String user;
	private String pass;
	
	public AsteriskTAPIDriver(String host, String user, String pass) {
		super();
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.eventProvider = new AsteriskEventProvider();
	}

	@Override
	public EventProvider createEventProvider() {
		return eventProvider;
	}
	
	@Override
	public void init() {
		startConnection();		
	}

	@Override
	public boolean isConnected() {
		return connected;
	}
	
	@Override
	public void close() {
		shutdown = true;
		if (pingThread != null && pingThread.isAlive()) {
			pingThread.die();
			if (connection != null && ( 
					ManagerConnectionState.CONNECTED.equals(connection.getState()) ||
					ManagerConnectionState.CONNECTING.equals(connection.getState()))) {
				connection.logoff();
			}
		}
		eventProvider.connectionInterrupted(null);
	}

	private void startConnection() {
		Thread t = new Thread() {
			
			{
				setName("Asterisk-Connection-checker");
			}
			
			@Override
			public void run() {
				super.run();
				
				while (!shutdown) {
					
					do {
						
						// log. intento de conexión
						
						connected = connect(host, user, pass);
						
						// log. resultado.
						
						if (!connected)
							sleepTime();
						
					} while (!connected);
					
					pingThread = new PingThread(connection);
					pingThread.setTimeout(5*1000L);
					pingThread.setInterval(10*1000L);
					pingThread.start();
					
					
					// espero unos segundos a que levante asterisk
					sleepTime();sleepTime();
					sleepTime();sleepTime();
//					sleepTime();sleepTime();
					
					eventProvider.connectionEstablished(connection);
					
					while (ManagerConnectionState.CONNECTED.equals(connection.getState())) {
						sleepTime();
					}
					
					eventProvider.connectionInterrupted(connection);
					
					connected = false;
					try {
						pingThread.die();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						pingThread = null;
					}
					
					try {
						connection.logoff();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						connection = null;
					}
				}
			}
		};
		t.start();
	}
	
	private boolean connect(String hostname, String username, String password) {
		connection = new DefaultManagerConnection(hostname, username, password);
		
		try {
			connection.login();
			return true;
		} catch (TimeoutException e) {
			connection = null;
			return false;
		}
		catch (Exception e) {
			return false;
		}
		
	}
	
	protected void sleepTime() {
		try {
			// 1 seg de espera.
			Thread.sleep(1000L); 
		} catch (InterruptedException ie) {
			// no hago nada.
		}
	}
}
