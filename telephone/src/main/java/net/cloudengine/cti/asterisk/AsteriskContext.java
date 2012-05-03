package net.cloudengine.cti.asterisk;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.cti.Call;
import net.cloudengine.cti.CallListener;

public class AsteriskContext {

	private List<AbstractAsteriskCall> calls = new ArrayList<AbstractAsteriskCall>();
	private List<CallBuilder> builders = new ArrayList<CallBuilder>();
	private List<AbstractAsteriskCall> queueCalls = new ArrayList<AbstractAsteriskCall>();
	private CallEventHandler handler;
	
	public AsteriskContext(CallEventHandler handler) {
		super();
		this.handler = handler;
	}

	public CallEventHandler getHandler() {
		return handler;
	}

	public void setHandler(CallEventHandler handler) {
		this.handler = handler;
	}

	public void addCall(AbstractAsteriskCall call) {
		calls.add(call);
		for(CallListener listener : handler.getListeners()) {
			listener.onNewCall(call);
		}
	}
	
	public void addBuilder(CallBuilder builder) {
		builders.add(builder);
	}
	
	public void removeCall(Call call) {
		calls.remove(call);
		for(CallListener listener : handler.getListeners()) {
			listener.onCallFinish(call);
		}
	}
	
	public void queueCall(AbstractAsteriskCall call, String queue) {
		queueCalls.add(call);
		for(CallListener listener : handler.getListeners()) {
			listener.onQueueCall(call, queue);
		}
	}
	
	public void dequeueCall(AbstractAsteriskCall call, String queue) {
		queueCalls.remove(call);
		for(CallListener listener : handler.getListeners()) {
			listener.onDequeueCall(call, queue);
		}
	}
	
	public void removeBuilder(CallBuilder builder) {
		builders.remove(builder);
	}
	
	public AbstractAsteriskCall[] getCalls() {
		return calls.toArray(new AbstractAsteriskCall[0]);
	}
	
	public CallBuilder[] getFutureCalls() {
		return builders.toArray(new CallBuilder[0]);
	}
	
	public AbstractAsteriskCall[] getQueueCalls() {
		return queueCalls.toArray(new AbstractAsteriskCall[0]);
	}
	
	public void holdCall(Call call) {
		for(CallListener listener : handler.getListeners()) {
			listener.onChangeState(call);
		}
	}
	
	public void unHoldCall(Call call) {
		for(CallListener listener : handler.getListeners()) {
			listener.onChangeState(call);
		}
	}
}
