package net.cloudengine.web.map;

import net.cloudengine.api.jpa.dao.StreetBlockDao;
import net.cloudengine.api.jpa.dao.ZoneDao;
import net.cloudengine.model.map.Location;
import net.cloudengine.model.map.StreetBlock;
import net.cloudengine.model.map.geo.FatureCollection;
import net.cloudengine.service.admin.ConfigurationService;
import net.shapefile.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller para los mapas de pruebas.
 * @author German Ulrich
 *
 */
@Controller
public class TestMapController {
	
	private ConfigurationService service; 
	private ZoneDao zoneDao;
	private StreetBlockDao streetDao;
	
	@Autowired
	public TestMapController(ConfigurationService service, ZoneDao zoneDao, StreetBlockDao streetDao) {
		this.service = service;
		this.zoneDao = zoneDao;
		this.streetDao = streetDao;
	}
	
	@RequestMapping(value = "/map/test", method = RequestMethod.GET)
	public ModelAndView testMap() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("map.google.street", service.getProperty("map.google.street"));
		mav.addObject("map.osm", service.getProperty("map.osm"));
		mav.setViewName("/map/testMap");
		return mav;
	}
	
	@RequestMapping(value = "/map/zone/test", method = RequestMethod.GET)
	public ModelAndView testZoneMap() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("map.google.street", service.getProperty("map.google.street"));
		mav.addObject("map.osm", service.getProperty("map.osm"));
		mav.addObject("zones", zoneDao.getZonesType());
		mav.setViewName("/map/testZoneMap");
		return mav;
	}
	
	@RequestMapping(value = "/locate", method = RequestMethod.GET)
	public @ResponseBody Location find(@RequestParam("loc") String loc, @RequestParam("nro") int nro) {
		StreetBlock s = streetDao.find(loc, nro);
		Location c = new Location();
		if (s != null) {
			c.setName(s.getName()+" "+nro);
			Point p = s.pointForNumber(nro);
			c.setX(p.getX());
			c.setY(p.getY());
		}
		return c;
	}
	
	@RequestMapping(value = "/zone", method = RequestMethod.GET)
	public @ResponseBody FatureCollection getZones(@RequestParam("name") String name, @RequestParam("type") String type) {
		FatureCollection fc = new FatureCollection(zoneDao.getByNameAndType(name, type));
		return fc;
	}
	

}
