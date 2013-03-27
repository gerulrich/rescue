package net.cloudengine.rpc.controller.geo;

import java.util.List;

public interface ZoneController {
	
	List<String> getZoneTypes();
	List<ZoneModel> getZoneByType(String typeName);

}
