package net.cloudengine.cti.asterisk;

import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public class SinglePartyCall extends AbstractAsteriskCall  {

	private NewStateEvent nse;
	
	public SinglePartyCall(NewChannelEvent nce, NewStateEvent nse) {
		super(nce);
		this.nse = nse;
	}

	@Override
	public String getCallerId() {
		return nse.getCallerIdNum();
	}	

}
