package net.cloudengine.rpc.controller.resource;

import java.util.List;

import net.cloudengine.rpc.model.resource.ResourceModel;
import net.cloudengine.rpc.model.resource.ResourceTypeModel;

public interface ResourceController {
	
	
	void addPosition(String imei, double lon, double lat, double speed);

	List<ResourceTypeModel> getTypes();
	
	List<ResourceModel> getAll(long version);
	
	
}
