package net.cloudengine.rpc.service;

import java.util.Collection;
import java.util.List;

import net.cloudengine.management.ExternalService;
import net.cloudengine.model.resource.Resource;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.model.resource.WayPoint;
import net.cloudengine.rpc.controller.resource.ResourceController;
import net.cloudengine.rpc.mappers.DTOMapper;
import net.cloudengine.rpc.mappers.MappersRegistry;
import net.cloudengine.rpc.model.resource.ResourceModel;
import net.cloudengine.rpc.model.resource.ResourceTypeModel;
import net.cloudengine.service.ResourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ExternalService(exportedInterface=ResourceController.class)
public class ResourceControllerImpl implements ResourceController {

	private static final Logger logger = LoggerFactory.getLogger(ResourceControllerImpl.class);
	
	private ResourceService service;
	private MappersRegistry mappersRegistry;
	
	
	@Autowired
	public ResourceControllerImpl(ResourceService service, MappersRegistry mappersRegistry) {
		super();
		this.service = service;
		this.mappersRegistry = mappersRegistry;
	}

	@Override
	@Transactional
	public void addPosition(String imei, double lon, double lat, double speed) {
		if (logger.isDebugEnabled()) {
			logger.debug("Registrando posición para imei {}, lon: {}, lat: {}", 
					new Object[] {imei, lon, lat});
		}
		Resource resource = service.getByImei(imei);
		if (resource != null) {
			WayPoint wp = resource.trackPoint(lon, lat, speed);
			service.save(wp);
			service.update(resource);
		}
	}

	@Override
	@Transactional
	public List<ResourceTypeModel> getTypes() {
		DTOMapper mapper = mappersRegistry.getMapper(ResourceTypeModel.class);
		Collection<ResourceType> resourcesTypes = service.getAllTypes(); 
		return mapper.fillModels(resourcesTypes, ResourceTypeModel.class);
	}
	
	@Override
	@Transactional
	public List<ResourceModel> getAll(long version) {
		DTOMapper mapper = mappersRegistry.getMapper(ResourceModel.class);
		Collection<Resource> list = service.getAll(version);
		return mapper.fillModels(list, ResourceModel.class);
	}

}
