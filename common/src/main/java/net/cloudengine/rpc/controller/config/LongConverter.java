package net.cloudengine.rpc.controller.config;

public class LongConverter implements Converter<Long> {

	public Long convert(String value) {
		return Long.parseLong(value);
	}

}
