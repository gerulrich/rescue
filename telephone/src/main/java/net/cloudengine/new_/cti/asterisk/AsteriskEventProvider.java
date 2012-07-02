package net.cloudengine.new_.cti.asterisk;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.cti.asterisk.AbstractAsteriskCall;
import net.cloudengine.cti.asterisk.CallBuilder;
import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.pbx.Status;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;

public class AsteriskEventProvider extends AbstractManagerEventListener implements EventProvider {

	private List<EventListener> listeners = new ArrayList<EventListener>();
	
	private List<AbstractAsteriskCall> calls = new ArrayList<AbstractAsteriskCall>();
	private List<AbstractAsteriskCall> queuedCalls = new ArrayList<AbstractAsteriskCall>();
	private List<CallBuilder> builders = new ArrayList<CallBuilder>();
	
	@Override
	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(EventListener listener) {
		listeners.remove(listener);
	}
	
	public void connectionEstablished(ManagerConnection manager) {
		cleanUp();
		for (EventListener listener : listeners) {
			listener.onConnect();
		}
		manager.addEventListener(this);
		// TODO agregar listeners
	}
	
	public void connectionInterrupted(ManagerConnection manager) {
		cleanUp();
		for (EventListener listener : listeners) {
			listener.onDisconnect();
		}
	}
	
	private void cleanUp() {
		
	}
	
	
	
	
	@Override
	public void onManagerEvent(ManagerEvent event) {
		super.onManagerEvent(event);
		
		// -- 
		boolean processed = false;
		for (AbstractAsteriskCall call : calls) {
//			processed = call.handleEvent(event, context);
			if (processed) {
				break;
			}
		}
		
		for (AbstractAsteriskCall call : queuedCalls) {
//			processed = call.handleEvent(event, context);
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
		
		for (CallBuilder callBuilder : builders) {
//			processed = callBuilder.handleEvent(event, context);
			if (processed) {
				if (!callBuilder.isValid()) {
//					System.out.println("Elimino CallBuilder");
//					context.removeBuilder(callConstruction); FIXME
					builders.remove(callBuilder);
					
				}
				break;
			}
		}

		if (!processed) {
			if (event instanceof NewChannelEvent) {
				NewChannelEvent nce = (NewChannelEvent) event;
				CallBuilder callBilder = new CallBuilder(nce);
//				context.addBuilder(futureCall); FIXME llamar a los listeners
				builders.add(callBilder);
			}
		}
	}	

	/**
	 * Notifica a los listener el cambio de estado de la extension telefonica.
	 */
	@Override
	public void handleEvent(ExtensionStatusEvent event) {
		Status status = Status.fromInt(event.getStatus());
		for (EventListener listener : listeners) {
			listener.extensionChanged(event.getExten(), status);
		}
	}
	
}
