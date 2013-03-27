package net.cloudengine.new_.cti;

public interface TAPIDriver {
	
	void init();
	EventProvider createEventProvider();
	boolean isConnected();

	void close();

}
