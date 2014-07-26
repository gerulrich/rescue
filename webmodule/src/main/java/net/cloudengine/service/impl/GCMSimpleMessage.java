package net.cloudengine.service.impl;

import net.cloudengine.service.GCMMessage;

public class GCMSimpleMessage implements GCMMessage {

	private String type;
	private String body;
	
	public GCMSimpleMessage(String type, String body) {
		super();
		this.type = type;
		this.body = body;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getBody() {
		return body;
	}

}
