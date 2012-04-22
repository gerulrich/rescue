package net.cloudengine.cti;

public interface CallListener {
	
	void onNewCall(Call call);
	
	void onCallFinish(Call call);
	
	void onChangeState(Call call);

}
