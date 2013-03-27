package net.cloudengine.new_.cti.model.asterisk;

import net.cloudengine.new_.cti.asterisk.EventContextProvider;

import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public abstract class EventHandlerAdapter {
	
	public boolean handleEvent(ManagerEvent event, EventContextProvider context) {
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
		} else if (event instanceof JoinEvent) {
			return handle((JoinEvent)event, context);
		}  else if (event instanceof LeaveEvent) {
			return handle((LeaveEvent)event, context);
		}
		return false;
	}
	
	protected boolean handle(NewChannelEvent event, EventContextProvider context) {
		return false;
	}
	
	protected boolean handle(HangupEvent event, EventContextProvider context) {
		return false;
	}
	
	protected boolean handle(NewStateEvent event, EventContextProvider context) {
		return false;
	}
	
	protected boolean handle(NewExtenEvent event, EventContextProvider context) {
		return false;
	}
	
	protected boolean handle(HoldEvent event, EventContextProvider context) {
		return false;
	}
	
	protected boolean handle(MusicOnHoldEvent event, EventContextProvider context) {
		return false;
	}
	
	protected boolean handle(JoinEvent event, EventContextProvider context) {
		return false;
	}
	
	protected boolean handle(LeaveEvent event, EventContextProvider context) {
		return false;
	}

}
