package net.cloudengine.new_.cti.model.asterisk;

import java.util.Stack;

import net.cloudengine.new_.cti.asterisk.EventContextProvider;

import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public class CallCreator extends EventHandlerAdapter {
	
	private NewChannelEvent nce;
	private boolean twoPartyCall = false;
	private boolean valid = true;
	
	private Stack<ManagerEvent> eventStack = new Stack<ManagerEvent>();
	
	public CallCreator(NewChannelEvent nce) {
		super();
		this.nce = nce;
		eventStack.push(nce);
	}
	
	@Override
	protected boolean handle(NewStateEvent event, EventContextProvider context) {
		if (event.getUniqueId().equals(nce.getUniqueId())) {
			String state = event.getChannelStateDesc(); 
			if (state.equals("Up") && !twoPartyCall) {
				
//				System.out.println("Single party call"); // FIXME sacar por log.
				
				String id = nce.getUniqueId();
				String callerId = event.getCallerIdNum();
				String calledId = nce.getExten();
				
				SinglePartyCallImpl call = new SinglePartyCallImpl(id, callerId, calledId);
				context.addCall(call);
				
				valid = false;
				return true;
			} if (state.equals("Up") && twoPartyCall) {
				System.out.println("Two party call");  // FIXME sacar por log.
				
//				System.out.println("Two party call");
//				TwoPartyCall call = new TwoPartyCall(nce, event);
//				context.addCall(call);
//				valid = false;
//				return true;
				
				
				String id = nce.getUniqueId();
				String callerId = event.getCallerIdNum();
				String calledId = nce.getExten();
				
				TwoPartyCallImpl call = new TwoPartyCallImpl(id, callerId, calledId);
				context.addCall(call);
				valid = false;
				return true;
			} else if (state.equals("Ring")) {
				System.out.println("comienza una llamada");
//				twoPartyCall = true;
//				valid = false;
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean handle(HangupEvent event, EventContextProvider context) {
		if (event.getUniqueId().equals(nce.getUniqueId())) {
			context.removeCreator(this);
			valid = false;
			return true;
		}
		return false;
	}

	@Override
	protected boolean handle(NewExtenEvent event, EventContextProvider context) {
		if(event.getUniqueId().equals(nce.getUniqueId())) {
			if("Dial".equals(event.getApplication())) {
//				TwoPartyCall call = new TwoPartyCall(nce, event);
//				System.out.println("Two party call");
//				context.addCall(call);
				return true;
			}
		}
		return true;
	}

	@Override
	protected boolean handle(NewChannelEvent event, EventContextProvider context) {
		if (event.getUniqueId().equals(nce.getUniqueId())) {

			
		} else if (event.getChannel().contains("/"+nce.getExten())) {
			twoPartyCall = true;
			return true;
			// caller id
			// extent nce
		}
		return false;
	}
	
	public boolean isValid() {
		return valid;
	}
	
}