package net.cloudengine.rpc.mappers;

public class IdentityTransformer implements ValueTransformer {

	@Override
	public Object transform(Object source) {
		return source;
	}

	@Override
	public Object inverse(Object target) {
		return target;
	}

}
