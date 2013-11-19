package net.cloudengine.rpc.mappers.transformers;

import net.cloudengine.rpc.mappers.ValueTransformer;

public class ObjectToString implements ValueTransformer {

	@Override
	public Object transform(Object source) {
		if (source != null)
			return source.toString();
		return null;
	}

	@Override
	public Object inverse(Object target) {
		if (target != null)
			return target.toString();
		return null;
	}

}
