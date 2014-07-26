package net.cloudengine.rpc.service;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.dao.jpa.StreetBlockRepository;
import net.cloudengine.dao.jpa.ZoneRepository;
import net.cloudengine.management.ExternalService;
import net.cloudengine.model.geo.StreetBlock;
import net.cloudengine.model.geo.Zone;
import net.cloudengine.rpc.controller.geo.GeoController;
import net.cloudengine.rpc.controller.geo.StreetBlockModel;
import net.cloudengine.rpc.controller.geo.ZoneModel;
import net.shapefile.geometry.WKTUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ExternalService(exportedInterface=GeoController.class)
public class GeoControllerImpl implements GeoController {

	private ZoneRepository zoneRepository;
	private StreetBlockRepository streetBlockRepository;
	
	@Autowired
	public GeoControllerImpl(ZoneRepository zoneRepository, StreetBlockRepository streetBlockRepository) {
		super();
		this.zoneRepository = zoneRepository;
		this.streetBlockRepository = streetBlockRepository;
	}

	@Override
	@Transactional
	public List<String> getZoneTypes() {
		return zoneRepository.getZonesType();
	}

	@Override
	@Transactional
	public List<ZoneModel> getZoneByType(String typeName) {
		List<ZoneModel> result = new ArrayList<ZoneModel>();
		List<Zone> zones = zoneRepository.getByType(typeName);
		for(Zone z : zones) {
			ZoneModel zm = new ZoneModel();
			zm.setId(z.getId());
			zm.setName(z.getName());
			zm.setType(z.getType());
			zm.setGeometry(WKTUtils.asText(z.getGeom()));
			result.add(zm);
		}
		return result;
	}
	
	@Override
	@Transactional
	public StreetBlockModel getStreetBlockByStartVertix(int startVertix) {
		StreetBlock streetBlock = streetBlockRepository.findStreetBlockByStartVertix(startVertix);
		if (streetBlock != null) {
			StreetBlockModel model = new StreetBlockModel();
			model.setId(streetBlock.getId());
			model.setName(streetBlock.getName());
			model.setVstart(streetBlock.getVstart());
			model.setVend(streetBlock.getVend());
			double x = streetBlock.getGeom().getCentroid().getCentroid().getX();
			double y = streetBlock.getGeom().getCentroid().getCentroid().getY();
			model.setX(x);
			model.setY(y);
			return model;
		}
		return null;
	}

}
