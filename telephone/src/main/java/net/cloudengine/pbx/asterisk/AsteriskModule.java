package net.cloudengine.pbx.asterisk;

import net.cloudengine.cti.CallsMonitor;
import net.cloudengine.cti.Connection;
import net.cloudengine.cti.PhoneBootstrap;
import net.cloudengine.cti.asterisk.AsteriskCallsMonitor;
import net.cloudengine.cti.asterisk.AsteriskConnection;
import net.cloudengine.cti.asterisk.SoftPhoneBootstrap;
import net.cloudengine.pbx.PBXMonitor;

import com.google.inject.AbstractModule;

public class AsteriskModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Connection.class).to(AsteriskConnection.class);
		bind(PBXMonitor.class).to(AsteriskPBXMonitor.class);
		bind(PhoneBootstrap.class).to(SoftPhoneBootstrap.class);
		bind(CallsMonitor.class).to(AsteriskCallsMonitor.class);
	}

}
