package net.cloudengine.rpc.controller.geo;

import java.util.List;

public interface GeoController {
	
	/**
	 * Obtiene una lista con todos los tipos de zonas existentes
	 * @return
	 */
	List<String> getZoneTypes();
	
	/**
	 * Obtiene una lista de zonas filtrando por un tipo de zona.
	 * @param typeName
	 * @return
	 */
	List<ZoneModel> getZoneByType(String typeName);
	
	
	StreetBlockModel getStreetBlockByStartVertix(int startVertix);

}
