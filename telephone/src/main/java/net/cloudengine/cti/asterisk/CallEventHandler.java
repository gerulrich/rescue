package net.cloudengine.cti.asterisk;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.cti.CallListener;

import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public class CallEventHandler implements ManagerEventListener {

	private AsteriskContext context = new AsteriskContext(this);
	private List<String> classes = new ArrayList<String>();
	
	private List<CallListener> listeners = new ArrayList<CallListener>();

	public CallEventHandler() {
		super();
		classes.add("VarSetEvent");
//		classes.add("NewExtenEvent");
		classes.add("RtcpSentEvent");
		classes.add("RtcpReceivedEvent");
		classes.add("PeerStatusEvent");
		classes.add("JitterBufStatsEvent");
		classes.add("BridgeEvent");
		classes.add("NewAccountCodeEvent");
	}

	@Override
	public void onManagerEvent(ManagerEvent event) {
		synchronized (this) {
			String className = event.getClass().getSimpleName();
			if (!classes.contains(className)) {
				if ("NewStateEvent".equals(className)) {
					System.out.println("==========================" + className + " - "+((NewStateEvent)event).getChannelStateDesc());
				} else if ("NewExtenEvent".equals(className)) {
					List<String> pp = new ArrayList<String>();
					pp.add("Set");
					pp.add("Macro");
					pp.add("GotoIf");
					pp.add("GosubIf");
				
					String desc = ((NewStateEvent)event).getChannelStateDesc();
					if (!pp.contains(desc))
						System.out.println("==========================" + className + " - "+((NewExtenEvent)event).getApplication());
				} else if ("NewChannelEvent".equals(className)) { 
					System.out.println("==========================" + className+" "+((NewChannelEvent)event).getUniqueId());
				} else {
					System.out.println("==========================" + className);
				}
			}
		}
		
		boolean processed = false;
		for (AbstractAsteriskCall call : context.getCalls()) {
			processed = call.handleEvent(event, context);
			if (processed) {
				break;
			}
		}

		if (!processed) {
			processEvent(event);
		}
	}

	private void processEvent(ManagerEvent event) {
		boolean processed = false;
		
		for (CallBuilder callConstruction : context.getFutureCalls()) {
			processed = callConstruction.handleEvent(event, context);
			if (processed) {
				if (!callConstruction.isValid()) {
					System.out.println("Elimino CallBuilder");
					context.removeBuilder(callConstruction);
				}
				break;
			}
		}

		if (!processed) {
			if (event instanceof NewChannelEvent) {
				NewChannelEvent nce = (NewChannelEvent) event;
				CallBuilder futureCall = new CallBuilder(nce);
				context.addBuilder(futureCall);
			}
		}
	}
	
	public void addListener(CallListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(CallListener listener) {
		listeners.remove(listener);
	}

	public List<CallListener> getListeners() {
		return listeners;
	}

}
