package net.cloudengine.cti.asterisk;

import java.io.IOException;

import net.cloudengine.cti.Call;
import net.cloudengine.cti.CallListener;
import net.cloudengine.cti.CallsMonitor;
import net.cloudengine.cti.Connection;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.MonitorAction;

import com.google.inject.Inject;

public class AsteriskCallsMonitor implements CallsMonitor {

	private Connection connection;
	private CallEventHandler handler;
	
	@Inject
	public AsteriskCallsMonitor(Connection connection) {
		this.connection = connection;
		this.handler = new CallEventHandler();
		this.connection.register(handler);
	}
	
	@Override
	public void addListener(CallListener listener) {
		handler.addListener(listener);
	}

	@Override
	public void removreListener(CallListener listener) {
		handler.removeListener(listener);
	}

	@Override
	public void hungap(Call call) {
		HangupAction ha = new HangupAction(call.getId()); // FIXME

		try {
			this.connection.getAsteriskServer().getManagerConnection().sendAction(ha);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void record(Call call) {
		MonitorAction monitor = new MonitorAction(call.getId(), "/var/spool/asterisk/monitor/custom-"+call.getId(), "wav", true); // FIXME, true

		try {
			this.connection.getAsteriskServer().getManagerConnection().sendAction(monitor);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
