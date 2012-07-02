package net.cloudengine.new_.cti;

public interface EventProvider {

	void addListener(EventListener listener);
	void removeListener(EventListener listener);
	
}
