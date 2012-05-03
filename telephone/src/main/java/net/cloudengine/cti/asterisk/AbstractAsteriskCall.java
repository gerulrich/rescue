package net.cloudengine.cti.asterisk;

import java.util.Date;

import net.cloudengine.cti.Call;

import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;
import org.asteriskjava.manager.event.NewChannelEvent;


public abstract class AbstractAsteriskCall extends EventHandlerAdapter implements Call {
	
	private NewChannelEvent nce;
	private boolean hold;
	
	
	public AbstractAsteriskCall(NewChannelEvent event) {
		this.nce = event;
	}
	
	protected String getUniqueId() {
		return this.nce.getUniqueId();
	}
	
	@Override
	public String getId() {
		return nce.getUniqueId();
	}

	@Override
	public String getCallerId() {
		return "-";
	}

	@Override
	public String getCalledId() {
		return nce.getExten();
	}
	
	@Override
	protected boolean handle(HangupEvent event, AsteriskContext context) {
		if (event.getUniqueId().equals(this.nce.getUniqueId())) {
			context.removeCall(this);
			return true;
		}
		return false;
	}

	@Override
	protected boolean handle(HoldEvent event, AsteriskContext context) {
		// FIXME
		if (event.getUniqueId().equals(this.getUniqueId())) {
			hold = event.isHold();
			if (hold) {
				context.holdCall(this);
			} else {
				context.unHoldCall(this);
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean handle(MusicOnHoldEvent event, AsteriskContext context) {
		// FIXME
		if (event.getUniqueId().equals(this.getUniqueId())) {
			hold = event.isStart();
			if (hold) {
				context.holdCall(this);
			} else {
				context.unHoldCall(this);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isHold() {
		return hold;
	}

	@Override
	public Date creationDate() {
		return nce.getDateReceived();
	}
	
	
}
