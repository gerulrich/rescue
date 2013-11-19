package net.cloudengine.util;

import java.util.List;

public class CasteableImpl implements Casteable {

	private Object object;
	
	public CasteableImpl(Object object) {
		super();
		this.object = object;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> asList(Class<T> clazz) {
		return (List)object;
	}

	@Override
	public Integer asInt() {
		return (Integer)object;
	}
	
	@Override
	public Long asLong() {
		return (Long)object;
	}

	@Override
	public String asString() {
		return (String)object;
	}	

}
