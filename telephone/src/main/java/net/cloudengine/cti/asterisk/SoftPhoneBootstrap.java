package net.cloudengine.cti.asterisk;

import java.io.IOException;

import net.cloudengine.cti.PhoneBootstrap;
import net.cloudengine.rpc.controller.config.PropertyController;

import com.google.inject.Inject;

public class SoftPhoneBootstrap implements PhoneBootstrap {

	private PropertyController controller;
	private Process process;
	
	@Inject
	public SoftPhoneBootstrap(PropertyController controller) {
		this.controller = controller;
	}
	
	@Override
	public void init() {
		// TODO log4j
		String command = controller.getProperty("cti.softphone").getValue();
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void shutdown() {
		// TODO log4j
		if (process != null) {
			process.destroy();
			return;
		}
		throw new IllegalStateException("No se puede detener el softphone antes de iniciarlo");
	}
}
