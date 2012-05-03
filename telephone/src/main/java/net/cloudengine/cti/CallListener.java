package net.cloudengine.cti;

public interface CallListener {
	
	void onNewCall(Call call);
	
	void onCallFinish(Call call);
	
	void onChangeState(Call call);
	
	void onQueueCall(Call call, String queue);
	
	void onDequeueCall(Call call, String queue);

}
