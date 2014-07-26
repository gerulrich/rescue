package net.cloudengine.web.crud.support;


public class LongIdParser implements IdParser<Long> {

	@Override
	public Long parse(String text) {
		return Long.parseLong(text);
	}

}
