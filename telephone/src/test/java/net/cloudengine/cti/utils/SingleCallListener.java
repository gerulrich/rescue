package net.cloudengine.cti.utils;

import net.cloudengine.cti.Call;
import net.cloudengine.cti.CallListener;

public class SingleCallListener implements CallListener {

	private Call call;
	private boolean hungupCall = false;

	@Override
	public void onNewCall(Call call) {
		this.call = call;
	}

	@Override
	public void onCallFinish(Call call) {
		if (this.call == call) {
			this.hungupCall = true;
		}
	}
	
	@Override
	public void onChangeState(Call call) {
		// TODO Auto-generated method stub
		
	}

	public Call getCall() {
		return call;
	}

	public boolean isHungupCall() {
		return hungupCall;
	}

	@Override
	public void onQueueCall(Call call, String queue) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDequeueCall(Call call, String queue) {
		// TODO Auto-generated method stub
	}
}