package net.cloudengine.cti.utils;

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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;

public class EventGenerator extends Thread {

	private static final String SEPARATOR = "--------------------------------------------------------";
	
	private ManagerEventListener listener;
	private File file;
	private Date lastEventDate;
	private boolean realTime;

	public EventGenerator(String fileName) {
		this(fileName, false);
	}
	
	public EventGenerator(String fileName, boolean realTime) {
		this.file = new File(fileName);
		this.realTime = realTime;
		if (!file.exists()) {
			throw new IllegalArgumentException("El archivo especificado no existe");
		}
	}
	
	public void setListener(ManagerEventListener listener) {
		this.listener = listener;
	}



	@Override
	public void run() {
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
		listener.onManagerEvent(event);
	}	
}