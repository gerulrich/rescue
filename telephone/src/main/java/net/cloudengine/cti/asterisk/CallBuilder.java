package net.cloudengine.cti.asterisk;

import java.util.Stack;

import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;

public class CallBuilder extends EventHandlerAdapter {
	
	private NewChannelEvent nce;
	private boolean twoPartyCall = false;
	private boolean valid = true;
	
	private Stack<ManagerEvent> eventStack = new Stack<ManagerEvent>();
	
	public CallBuilder(NewChannelEvent nce) {
		super();
		this.nce = nce;
		eventStack.push(nce);
	}

	@Override
	protected boolean handle(NewStateEvent event, AsteriskContext context) {
		if (event.getUniqueId().equals(nce.getUniqueId())) {
			String state = event.getChannelStateDesc(); 
			if (state.equals("Up") && !twoPartyCall) {
				System.out.println("Single party call");
				SinglePartyCall call = new SinglePartyCall(this.nce, event);
				context.addCall(call);
				valid = false;
				return true;
			} if (state.equals("Up") && twoPartyCall) {
				System.out.println("Two party call");
				TwoPartyCall call = new TwoPartyCall(nce, event);
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
	protected boolean handle(HangupEvent event, AsteriskContext context) {
		if (event.getUniqueId().equals(nce.getUniqueId())) {
			context.removeBuilder(this);
			valid = false;
			return true;
		}
		return false;
	}

	@Override
	protected boolean handle(NewExtenEvent event, AsteriskContext context) {
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
	protected boolean handle(NewChannelEvent event, AsteriskContext context) {
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
