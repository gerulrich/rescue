package net.cloudengine.rpc.controller.config;

public class BooleanConverter implements Converter<Boolean> {

	public Boolean convert(String value) {
		return Boolean.parseBoolean(value);
	}

}
