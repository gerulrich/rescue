package net.cloudengine.new_.cti.model.asterisk;

import java.util.Date;

import net.cloudengine.new_.cti.asterisk.EventContextProvider;
import net.cloudengine.new_.cti.model.Call;

import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;

public abstract class AbstractCallAsteriskImpl extends EventHandlerAdapter implements Call {

	private String id;
	private String callerId;
	private String calledId;
	private boolean hold;
	private Date creationDate = new Date();
	
	public AbstractCallAsteriskImpl(String id, String callerId, String calledId) {
		super();
		this.id = id;
		this.callerId = callerId;
		this.calledId = calledId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getCallerId() {
		return callerId;
	}

	@Override
	public String getCalledId() {
		return calledId;
	}

	@Override
	public boolean isHold() {
		return hold;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}
	
	@Override
	protected boolean handle(HangupEvent event, EventContextProvider context) {
		if (event.getUniqueId().equals(getId())) {
			context.removeCall(this);
			return true;
		}
		return false;
	}

	@Override
	protected boolean handle(HoldEvent event, EventContextProvider context) {
		// FIXME
		if (event.getUniqueId().equals(getId())) {
			hold = event.isHold();
			if (hold) {
//				context.holdCall(this);
			} else {
//				context.unHoldCall(this);
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean handle(MusicOnHoldEvent event, EventContextProvider context) {
		// FIXME
		if (event.getUniqueId().equals(getId())) {
			hold = event.isStart();
			if (hold) {
//				context.holdCall(this);
			} else {
//				context.unHoldCall(this);
			}
			return true;
		}
		return false;
	}
}
