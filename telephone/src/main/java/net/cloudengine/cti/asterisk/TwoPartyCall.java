package net.cloudengine.cti.asterisk;

import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public class TwoPartyCall extends AbstractAsteriskCall {
	
	private NewStateEvent nse;
	
	public TwoPartyCall(NewChannelEvent event, NewStateEvent nse) {
		super(event);
		this.nse = nse;
	}

	@Override
	public String getCallerId() {
		return nse.getCallerId();
	}

	@Override
	protected boolean handle(NewChannelEvent event, AsteriskContext context) {
		if (event.getUniqueId().equals(nse.getUniqueId())) {
			return true;
		} else {
			return false;
		}
	}

}
