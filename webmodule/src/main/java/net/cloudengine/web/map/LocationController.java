package net.cloudengine.web.map;

import net.cloudengine.api.jpa.dao.StreetDao;
import net.cloudengine.model.map.Location;
import net.cloudengine.model.map.Street;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vividsolutions.jts.geom.Geometry;


@Controller
public class LocationController {
	
	private StreetDao dao;
	
	@Autowired
	public LocationController(StreetDao dao) {
		super();
		this.dao = dao;
	}

	@RequestMapping(value = "/locate", method = RequestMethod.GET)
	public @ResponseBody Location find(@RequestParam("loc") String loc, @RequestParam("nro") int nro) {
		Street s = dao.find(loc, nro);
		Location c = new Location();
		if (s != null) {
			c.setName(s.getName());
			Geometry g = s.getGeom();
			c.setX(g.getCoordinates()[0].x);
			c.setY(g.getCoordinates()[0].y);
		}
		return c;
	}

}
