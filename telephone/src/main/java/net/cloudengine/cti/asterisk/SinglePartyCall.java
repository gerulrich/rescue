package net.cloudengine.cti.asterisk;

import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
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

	@Override
	protected boolean handle(JoinEvent event, AsteriskContext context) {
		if (event.getUniqueId().equals(nse.getUniqueId())) {
			context.removeCall(this);
			context.queueCall(this, event.getQueue());
			return true;
		}
		return false;
	}

	@Override
	protected boolean handle(LeaveEvent event, AsteriskContext context) {
		if (event.getUniqueId().equals(nse.getUniqueId())) {
			context.dequeueCall(this, event.getQueue());
			return true;
		} 
		return false;
	}

}
