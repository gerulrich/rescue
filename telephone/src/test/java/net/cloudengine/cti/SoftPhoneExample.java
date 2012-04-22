package net.cloudengine.cti;

import net.cloudengine.pbx.asterisk.AsteriskModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SoftPhoneExample {
	
	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(new AsteriskModule());
		PhoneBootstrap boot = injector.getInstance(PhoneBootstrap.class);
		boot.init();
		Thread.sleep(30000);
		boot.shutdown();
	}

}
