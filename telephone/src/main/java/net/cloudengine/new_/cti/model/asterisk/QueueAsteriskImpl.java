package net.cloudengine.new_.cti.model.asterisk;

import net.cloudengine.new_.cti.model.Queue;


public class QueueAsteriskImpl implements Queue {

	private String name;
	
	public QueueAsteriskImpl(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
