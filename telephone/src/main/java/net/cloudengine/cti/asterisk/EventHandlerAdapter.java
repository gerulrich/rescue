package net.cloudengine.cti.asterisk;

import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public abstract class EventHandlerAdapter {

	public boolean handleEvent(ManagerEvent event, AsteriskContext context) {
		if (event instanceof NewChannelEvent) {
			return handle((NewChannelEvent)event, context);
		} else if (event instanceof HangupEvent) {
			return handle((HangupEvent)event, context);
		} else if (event instanceof NewStateEvent) {
			return handle((NewStateEvent)event, context);
		} else if (event instanceof NewExtenEvent) {
			return handle((NewExtenEvent)event, context);
		} else if (event instanceof HoldEvent) {
			return handle((HoldEvent)event, context);
		} else if (event instanceof MusicOnHoldEvent) {
			return handle((MusicOnHoldEvent)event, context);
		}
		return false;
	}
	
	protected boolean handle(NewChannelEvent event, AsteriskContext context) {
		return false;
	}
	
	protected boolean handle(HangupEvent event, AsteriskContext context) {
		return false;
	}
	
	protected boolean handle(NewStateEvent event, AsteriskContext context) {
		return false;
	}
	
	protected boolean handle(NewExtenEvent event, AsteriskContext context) {
		return false;
	}
	
	protected boolean handle(HoldEvent event, AsteriskContext context) {
		return false;
	}
	
	protected boolean handle(MusicOnHoldEvent event, AsteriskContext context) {
		return false;
	}

}
