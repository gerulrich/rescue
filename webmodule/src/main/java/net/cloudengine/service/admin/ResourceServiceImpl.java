package net.cloudengine.service.admin;

import java.util.Collection;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.PagingResult;
import net.cloudengine.api.jpa.dao.ResourceDao;
import net.cloudengine.api.jpa.dao.ResourceTypeDao;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.model.resource.Resource;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.model.resource.WayPoint;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

	private static final String RESOURCE_SEQ = "RESOURCE_SEQ";
	private ResourceTypeDao typeDao;
	private ResourceDao dao;
	private Datastore<FileDescriptor, ObjectId> fileStore;
	private Datastore<WayPoint, ObjectId> wayPointStore;
	private SqlService sqlService;
	
	@Autowired
	public ResourceServiceImpl(ResourceTypeDao typeDao, ResourceDao dao,
			@Qualifier("fileStore") Datastore<FileDescriptor, ObjectId> fileStore,
			@Qualifier("wayPointStore") Datastore<WayPoint, ObjectId> wayPointStore,
			SqlService sqlService) {
		this.typeDao = typeDao;
		this.fileStore = fileStore;
		this.wayPointStore = wayPointStore;
		this.dao = dao;
		this.sqlService = sqlService;
	}
	
	@Override
	public Resource get(Long id) {
		return dao.get(id);
	}
	
	@Override
	public void save(Resource resource) {
		resource.setVersion(sqlService.nextVal(RESOURCE_SEQ));
		dao.save(resource);
	}
	
	@Override
	public void update(Resource resource) {
		resource.setVersion(sqlService.nextVal(RESOURCE_SEQ));
		dao.save(resource);
	}

	@Override
	public PagingResult<Resource> getAll(int page, int size) {
		return dao.list(page, size);
	}
	
	@Override
	public Collection<Resource> getAll(long version) {
		return dao.createQuery()
				.field("version").gt(version)
				.list();
	}

	public Resource getByImei(String imei) {
		return dao.createQuery().field("imei").eq(imei).get();
	}
	
	@Override
	public ResourceType getType(Long id) {
		return typeDao.get(id);
	}

	@Override
	public void save(ResourceType type) {
		typeDao.save(type);
	}

	@Override
	public void update(ResourceType type) {
		typeDao.update(type);
	}
	
	@Override
	public void deleteType(Long id) {
		typeDao.delete(id);
	}
	
	@Override
	public void delete(Long id) {
		dao.delete(id);
	}	

	@Override
	public PagingResult<ResourceType> getAllTypes(int page, int size) {
		return typeDao.list(page, size);
	}

	@Override
	public Collection<FileDescriptor> getAllImages() {
		return fileStore.createQuery().field("type").eq("png").list();
	}

	@Override
	public FileDescriptor getImage(ObjectId id) {
		return fileStore.get(id);
	}

	@Override
	public void save(WayPoint wayPoint) {
		wayPointStore.save(wayPoint);
	}

}
