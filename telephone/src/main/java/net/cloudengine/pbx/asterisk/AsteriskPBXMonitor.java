package net.cloudengine.pbx.asterisk;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.StringTokenizer;

import net.cloudengine.cti.Connection;
import net.cloudengine.pbx.Group;
import net.cloudengine.pbx.PBXMonitor;
import net.cloudengine.pbx.PhoneExt;
import net.cloudengine.pbx.PhoneStatusListener;
import net.cloudengine.pbx.Status;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.manager.action.ExtensionStateAction;
import org.asteriskjava.manager.response.ExtensionStateResponse;

import com.google.inject.Inject;


public class AsteriskPBXMonitor implements PBXMonitor {
	
	public static final Group AVAILABLE = new Group("Conectadas");
	public static final Group UNAVAILABLE = new Group("Desconectadas", Status.UNAVAILABLE);
	private Collection<Group> groups = new LinkedList<Group>();
	
	private Connection connection;
	private AsteriskEventHandler handler;
	
	
	@Inject
	public AsteriskPBXMonitor(Connection connection) {
		this.connection = connection;
		this.handler = new AsteriskEventHandler();
		this.connection.register(handler);
		this.groups.add(AVAILABLE);
		this.groups.add(UNAVAILABLE);
	}
	
	public Collection<Group> getGroups() {
		return new LinkedList<Group>(groups);
	}

	public Collection<PhoneExt> getAllPhoneExt() {
		// Ejecutamos el comando en asterisk que corresponda
		if (!this.connection.isConnected()) {
			return new ArrayList<PhoneExt>();
		}
		AsteriskServer server = this.connection.getAsteriskServer(); 
		String instruccion = server.executeCliCommand("sip show peers").toString();
		String phones[] = instruccion.split(",");
		Collection<PhoneExt> result = new ArrayList<PhoneExt>();
		
		// la primera y las tres ultimas lineas las podemos omitir.
		for(int i = 1; i < phones.length-3; i++) {
			String phoneRow = phones[i];
			StringTokenizer st = new StringTokenizer(phoneRow, "/ ");
			String partes[] = new String[st.countTokens()];
			int j = 0;
			while (st.hasMoreTokens()) {
				partes[j] = st.nextToken();
				j++;
			}
			

			PhoneExt pe = new PhoneExt();
			pe.setNumber(partes[0]);
			pe.setType("SIP");
			
			
			ExtensionStateAction accion = new ExtensionStateAction();
			accion.setContext("from-internal");
			accion.setExten(pe.getNumber());
			
			try {
				ExtensionStateResponse respuesta = (ExtensionStateResponse) server.getManagerConnection()
						.sendAction(accion, 1000);
				int s = respuesta.getStatus();
				pe.setStatus(Status.fromInt(s));
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.add(pe);
		}
		
		String lines[] = server.executeCliCommand("iax2 show peers").toArray(new String[0]);
		for (int i = 1; i < lines.length-1; i++) {
			StringTokenizer st = new StringTokenizer(lines[i], "/ ");
			String partes[] = new String[st.countTokens()];
			int j = 0;
			while (st.hasMoreTokens()) {
				partes[j] = st.nextToken();
				j++;
			}
			
			PhoneExt pe = new PhoneExt();
			pe.setNumber(partes[0]);
			pe.setType("IAX");
			
			ExtensionStateAction accion = new ExtensionStateAction();
			accion.setContext("from-internal");
			accion.setExten(pe.getNumber());
			
			try {
				ExtensionStateResponse respuesta = (ExtensionStateResponse) server.getManagerConnection()
						.sendAction(accion, 1000);
				int s = respuesta.getStatus();
				pe.setStatus(Status.fromInt(s));
			} catch (Exception e) {
				e.printStackTrace();
			}			
			result.add(pe);
		}
		
		
		return result;
	}

	@Override
	public void addListener(PhoneStatusListener listener) {
		this.handler.addListener(listener);
	}
	
}
