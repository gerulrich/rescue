package net.cloudengine.new_.cti.model.asterisk;

import net.cloudengine.new_.cti.asterisk.EventContextProvider;

import org.asteriskjava.manager.event.NewChannelEvent;

public class TwoPartyCallImpl extends AbstractCallAsteriskImpl {

	public TwoPartyCallImpl(String id, String callerId, String calledId) {
		super(id, callerId, calledId);
	}
	
	@Override
	public boolean isInQueue() {
		return false;
	}
	
	@Override
	protected boolean handle(NewChannelEvent event, EventContextProvider context) {
		if (event.getUniqueId().equals(getId())) {
			return true;
		} else {
			return false;
		}
	}

}
