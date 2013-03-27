package net.cloudengine.new_.cti.model.asterisk;

import net.cloudengine.new_.cti.asterisk.EventContextProvider;

import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;

public class SinglePartyCallImpl extends AbstractCallAsteriskImpl {

	private boolean inQueue = false;
	
	public boolean isInQueue() {
		return inQueue;
	}

	public SinglePartyCallImpl(String id, String callerId, String calledId) {
		super(id, callerId, calledId);
	}	
	
	@Override
	protected boolean handle(JoinEvent event, EventContextProvider context) {
		if (event.getUniqueId().equals(getId())) {
			inQueue = true;
			context.changeCall(this);
			return true;
		}
		return false;
	}

	@Override
	protected boolean handle(LeaveEvent event, EventContextProvider context) {
		if (event.getUniqueId().equals(getId())) {
			inQueue = false;
			return true;
		}
		return false;
	}	
}
