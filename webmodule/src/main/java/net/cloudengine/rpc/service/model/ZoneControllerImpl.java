package net.cloudengine.rpc.service.model;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.api.jpa.dao.ZoneDao;
import net.cloudengine.management.ExternalService;
import net.cloudengine.model.geo.Zone;
import net.cloudengine.rpc.controller.geo.ZoneController;
import net.cloudengine.rpc.controller.geo.ZoneModel;
import net.shapefile.geometry.WKTUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ExternalService(exportedInterface=ZoneController.class)
public class ZoneControllerImpl implements ZoneController {

	private ZoneDao dao;
	
	@Autowired
	public ZoneControllerImpl(ZoneDao dao) {
		super();
		this.dao = dao;
	}

	@Override
	@Transactional
	public List<String> getZoneTypes() {
		return dao.getZonesType();
	}

	@Override
	@Transactional
	public List<ZoneModel> getZoneByType(String typeName) {
		List<ZoneModel> result = new ArrayList<ZoneModel>();
		List<Zone> zones = dao.getByType(typeName);
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

}
