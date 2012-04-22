package net.cloudengine.client.main;

import net.cloudengine.pbx.asterisk.AsteriskModule;

import com.google.inject.Module;

public interface GuiceModule {
	
	public Module BASE = new ServiceModule();
	public Module ASTERISK = new AsteriskModule();
}
