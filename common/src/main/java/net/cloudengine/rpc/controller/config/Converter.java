package net.cloudengine.rpc.controller.config;


public interface Converter<T> {
	
	T convert(String value);

}
