package net.cloudengine.cti.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public class EventDumper implements ManagerEventListener {

	private List<String> classes = new ArrayList<String>();
	
	public EventDumper(ManagerConnection connection) {
		classes.add("VarSetEvent");
		classes.add("RtcpSentEvent");
		classes.add("RtcpReceivedEvent");
		classes.add("PeerStatusEvent");
		classes.add("JitterBufStatsEvent");
		classes.add("BridgeEvent");
		classes.add("NewAccountCodeEvent");
		classes.add("DtmfEvent");
		classes.add("ChannelUpdateEvent");
		connection.addEventListener(this);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ManagerConnectionFactory factory = new ManagerConnectionFactory("192.168.0.102", "juan", "juan");
        ManagerConnection managerConnection = factory.createManagerConnection();
        managerConnection.login();
		new EventDumper(managerConnection);
		Thread.sleep(1000000000);
	}

	@Override
	public void onManagerEvent(ManagerEvent event) {
		String className = event.getClass().getSimpleName();
		if (!classes.contains(className)) {
			if ("NewStateEvent".equals(className)) {
				logEvent(event);
			} else if ("NewExtenEvent".equals(className)) {
				List<String> pp = new ArrayList<String>();
				pp.add("Set");
				pp.add("Macro");
				pp.add("GotoIf");
				pp.add("GosubIf");
				
				String desc = ((NewStateEvent)event).getChannelStateDesc();
				if (!pp.contains(desc))
					logEvent(event);
			} else {
				logEvent(event);
			}
		}
	}
	
	private void logEvent(ManagerEvent event) {
		System.err.println(event.getClass().getName());
		try {
			BeanInfo bi = Introspector.getBeanInfo(event.getClass(), Object.class);
			for(PropertyDescriptor pd : bi.getPropertyDescriptors()) {
				String name = pd.getName();
				
				
				Object value = pd.getReadMethod().invoke(event, new Object[0]);
				if (value instanceof Date) {
					value = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(value);
				}
				if (value != null)
					System.err.println(name+","+value);
				
			}
			
		} catch (Exception e) {
			
		}
		System.err.println("--------------------------------------------------------");
	}
}
