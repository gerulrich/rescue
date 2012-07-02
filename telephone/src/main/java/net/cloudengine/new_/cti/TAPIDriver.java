package net.cloudengine.new_.cti;

public interface TAPIDriver {
	
	// TODO agregar metodos para proporcionar url, user y pass similar a jdbc.
	// void init().
	EventProvider createEventProvider();
	boolean isConnected();
	
	
}
