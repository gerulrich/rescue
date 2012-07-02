package net.cloudengine.new_.cti;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.SendActionCallback;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;

public class EventFileReader implements ManagerConnection {

	private static final String SEPARATOR = "--------------------------------------------------------";
	private ManagerEventListener eventListener = null;
	private File file;
	private Date lastEventDate;
	private boolean realTime;
	
	public EventFileReader(String fileName, boolean realTime) {
		this.file = new File(fileName);
		this.realTime = realTime;
		if (!file.exists()) {
			throw new IllegalArgumentException("El archivo especificado no existe");
		}
	}
	
	/**
	 * Lee todos los eventos del archivos e invoca
	 */
	public void exec() {
		try {
			
			boolean newEvent = true;
			ManagerEvent me = null;
			
			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				if (newEvent) {
					newEvent = false;
					String classname = strLine;
					me = createEvent(classname);
				} else {
					if (SEPARATOR.equals(strLine)) {
						dispatchEvent(me);
						me = null;
						newEvent = true;
					} else {
						setProperty(me, strLine);
					}
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Crea una instancia del evento a partir del nombre de la clase.
	 * @param name nombre de la clase.
	 * @return instancia del evento.
	 */
	private ManagerEvent createEvent(String name) {
		try {
			Class<?> eventClass = Class.forName(name);
			Constructor<?> constructor = eventClass.getConstructor(Object.class);
			ManagerEvent me = (ManagerEvent) constructor.newInstance(this);
			return me;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setProperty(ManagerEvent event, String line) {
		String vals[] = line.split(",");
		String name = vals[0];
		String value = vals[1];
		try {
			BeanInfo bi = Introspector.getBeanInfo(event.getClass());
			for(PropertyDescriptor pd : bi.getPropertyDescriptors()) {
				if (pd.getName().equals(name)) {
					Method writer = pd.getWriteMethod();
					Class<?> paramsTypes[] = writer.getParameterTypes();
					if (paramsTypes[0].equals(String.class)) {
						writer.invoke(event, value);
					} else if (paramsTypes[0].equals(Integer.class)) {
						Integer v = new Integer(value);
						writer.invoke(event, v);
					} else if (paramsTypes[0].equals(Date.class)) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
						Date v = sdf.parse(value);
						writer.invoke(event, v);
					} else {
						throw new RuntimeException("Tipo no esperado, se debe agregar el mismo en el generador de eventos");
					}
				}				
			}
		} catch (Exception e) {
			
		}
	}
	
	private void dispatchEvent(ManagerEvent event) {
		if (lastEventDate != null) {
			if ( realTime ) {
				long diff = event.getDateReceived().getTime() - lastEventDate.getTime();
				try {
					Thread.sleep(diff);
				} catch (InterruptedException e) { }
			}			
		}  else {
			if (realTime) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) { }
			}
		}
		lastEventDate = event.getDateReceived();
		eventListener.onManagerEvent(event);
	}
	

	public void addEventListener(ManagerEventListener eventListener) { 
		if (this.eventListener != null) {
			throw new IllegalArgumentException("El mock solo admite un listener por instancia");
		}
		this.eventListener = eventListener;
	}
	
	
	// Metodos que me obliga a implementar la interface ManagerConnection.
	
	public String getHostname() { return null; }
	public int getPort() { return 0; }
	public String getUsername() { return null; }
	public String getPassword() { return null; }
	public AsteriskVersion getVersion() { return null; }
	public boolean isSsl() { return false; }
	public InetAddress getLocalAddress() { return null; }
	public int getLocalPort() { return 0; }
	public InetAddress getRemoteAddress() { return null; }
	public int getRemotePort() { return 0; }
	public void registerUserEventClass(Class<? extends ManagerEvent> userEventClass) { }
	public void setSocketTimeout(int socketTimeout) { }
	public void setSocketReadTimeout(int socketReadTimeout) { }
	public void login() { }
	public void login(String events) { }
	public void logoff() { }
	public String getProtocolIdentifier() { return null; }
	public ManagerConnectionState getState() { return null; }
	public ManagerResponse sendAction(ManagerAction action) { return null; }
	public ManagerResponse sendAction(ManagerAction action, long timeout) { return null; }
	public void sendAction(ManagerAction action, SendActionCallback callback)  { }
	public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action) { return null; }
	public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, long timeout) { return null; }
	public void removeEventListener(ManagerEventListener eventListener) { }

}
