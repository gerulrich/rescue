package net.cloudengine.widgets.task;

import java.util.TimerTask;

import net.cloudengine.widgets.sound.PhoneLine;

public class ReRegisterRetries extends TimerTask {

	private PhoneLine lines[];
	private int registerRetries;
	
	public ReRegisterRetries(PhoneLine[] lines) {
		super();
		this.lines = lines;
		registerRetries = 0;
	}

	@Override
	public void run() {

	}

}
