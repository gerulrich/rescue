package net.cloudengine.new_.cti;

import net.cloudengine.new_.cti.model.Call;

public interface EventProvider {

	void addListener(EventListener listener);
	void removeListener(EventListener listener);
	
	void hangup(Call call);
	void record(Call call);
	
}
