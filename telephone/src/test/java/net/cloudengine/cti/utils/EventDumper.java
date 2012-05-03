package net.cloudengine.cti.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.cloudengine.cti.Connection;
import net.cloudengine.pbx.asterisk.AsteriskModule;

import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewStateEvent;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class EventDumper implements ManagerEventListener {

	@Inject
	private Connection connection;
	private List<String> classes = new ArrayList<String>();
	
	public EventDumper() {
		classes.add("VarSetEvent");
		classes.add("RtcpSentEvent");
		classes.add("RtcpReceivedEvent");
		classes.add("PeerStatusEvent");
		classes.add("JitterBufStatsEvent");
		classes.add("BridgeEvent");
		classes.add("NewAccountCodeEvent");
		classes.add("DtmfEvent");
		classes.add("ChannelUpdateEvent");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(new AsteriskModule());
		EventDumper ed = injector.getInstance(EventDumper.class);
		ed.connection.getAsteriskServer().getManagerConnection().addEventListener(ed);
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

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
