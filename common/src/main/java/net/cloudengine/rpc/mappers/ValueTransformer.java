package net.cloudengine.rpc.mappers;

public interface ValueTransformer {
	
	Object transform(Object source);
	
	Object inverse(Object target);

}
