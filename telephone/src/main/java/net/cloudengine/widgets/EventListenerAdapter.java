package net.cloudengine.widgets;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.model.Call;
import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.QEntry;
import net.cloudengine.new_.cti.model.QMember;
import net.cloudengine.new_.cti.model.Queue;

public class EventListenerAdapter implements EventListener {

	@Override
	public void onConnect() {
		
	}

	@Override
	public void onDisconnect() {
		
	}

	@Override
	public void extensionChanged(Extension extension) {
		
	}

	@Override
	public void extensionAdded(Extension extension) {
		
	}

	@Override
	public void queueAdded(Queue queue) {
		
	}

	@Override
	public void queueMemberAdded(String queue, QMember member) {
		
	}

	@Override
	public void queueMemberRemoved(String queue, QMember member) {
		
	}

	@Override
	public void queueEntryAdded(String queue, QEntry entry) {
		
	}

	@Override
	public void queueEntryRemoved(String queue, QEntry entry) {
		
	}

	@Override
	public void newCall(Call call) {

	}
	
	@Override
	public void changeCall(Call call) {
		
	}

	@Override
	public void hangupCall(Call call) {

	}

}
