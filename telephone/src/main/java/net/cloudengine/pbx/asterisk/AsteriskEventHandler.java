package net.cloudengine.pbx.asterisk;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.pbx.PhoneStatusListener;
import net.cloudengine.pbx.Status;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.event.ExtensionStatusEvent;

public class AsteriskEventHandler extends AbstractManagerEventListener {

	private List<PhoneStatusListener> listeners = new ArrayList<PhoneStatusListener>();

	public void addListener(PhoneStatusListener listener) {
		listeners.add(listener);
	}

	public void removeListener(PhoneStatusListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void handleEvent(ExtensionStatusEvent event) {
		Status status = Status.fromInt(event.getStatus());
		for (PhoneStatusListener listener : listeners) {
			listener.onStatusChange(event.getExten(), status);
		}
	}
}