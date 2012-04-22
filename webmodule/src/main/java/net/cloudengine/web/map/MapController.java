package net.cloudengine.web.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.cloudengine.model.map.Tile;
import net.cloudengine.service.admin.ConfigurationService;
import net.cloudengine.service.map.TileCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MapController {
	
	private static Logger logger = LoggerFactory.getLogger(MapController.class);
	private static final String URI = "/tiles/{zoom}/{x}/{y}/{key}/";
	
	// zoom 16, x 22092, y 39464
	private TileCache tileCache;
	
	private ConcurrentMap<String, TileServer> servers = new ConcurrentHashMap<String, TileServer>();
	
	@Autowired
	public MapController(TileCache tileCache, ConfigurationService service) {
		super();
		this.tileCache = tileCache;
		servers.put("map.google.street", new KeyConfigTileServer("map.google.street", service));
		servers.put("map.google.sat", new KeyConfigTileServer("map.google.sat", service));
		servers.put("map.osm", new KeyConfigTileServer("map.osm", service));
	}

	@RequestMapping(value = URI, method = RequestMethod.GET)
	public ResponseEntity<byte[]> tile(@PathVariable("zoom") int zoom, @PathVariable("x") int x, 
			@PathVariable("y") int y, @PathVariable("key") String key) {
		if (!servers.containsKey(key)) {
			throw new IllegalArgumentException("La clave proporcionada no es válida: "+key);
		}
		
		TileServer tileServer = servers.get(key);
		
		logger.debug("Petición de tile, zoom = {}, x = {}, y = {}", new Object[] {zoom, x, y});
		
		// verifico si el tile está en el cache
		logger.debug("Buscando tile en el cache, zoom = {}, x = {}, y = {}", new Object[] {zoom, x, y});
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "image/png");
		
		Tile requestedTile = tileCache.get(zoom, x, y, tileServer.getKey());
		if (requestedTile != null) {
			logger.debug("Tile encontrado en el cache, zoom = {}, x = {}, y = {}", new Object[] {zoom, x, y});
			return new ResponseEntity<byte[]>(requestedTile.getImage(), responseHeaders, HttpStatus.OK);
		}
		
		Tile tile  = tileServer.get(zoom, x, y);

		if (tile.isCacheable()) {
			tileCache.put(tile);
		}
		return new ResponseEntity<byte[]>(tile.getImage(), responseHeaders, HttpStatus.OK);		
	}	
}
