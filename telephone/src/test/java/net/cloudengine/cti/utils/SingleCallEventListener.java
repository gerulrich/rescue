package net.cloudengine.cti.utils;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.model.Call;
import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.QEntry;
import net.cloudengine.new_.cti.model.QMember;
import net.cloudengine.new_.cti.model.Queue;

public class SingleCallEventListener implements EventListener {
	
	private Call call;
	private boolean connected = false;
	private boolean disconected = false;
	private boolean terminateCall = false;

	public Call getCall() {
		return call;
	}

	public boolean isConnected() {
		return connected;
	}

	public boolean isDisconected() {
		return disconected;
	}

	public boolean isTerminateCall() {
		return terminateCall;
	}

	@Override
	public void onConnect() {
		this.connected = true;		
	}

	@Override
	public void onDisconnect() {
		disconected = true;
	}

	@Override
	public void extensionChanged(Extension extension) {}

	@Override
	public void queueAdded(Queue queue) {}

	@Override
	public void queueMemberAdded(String queue, QMember member) {}

	@Override
	public void queueMemberRemoved(String queue, QMember member) {}

	@Override
	public void queueEntryAdded(String queue, QEntry entry) {}

	@Override
	public void newCall(Call call) {
		this.call = call;
	}

	@Override
	public void hangupCall(Call call) {
		if (this.call.getId().equals(call.getId())) {
			this.terminateCall = true;
		}
	}

	@Override
	public void extensionAdded(Extension extension) {
		
	}

	@Override
	public void queueEntryRemoved(String queue, QEntry entry) {
	
	}

	@Override
	public void changeCall(Call call) {
		
	}

}
