package net.cloudengine.service.impl;

import static net.cloudengine.dao.support.SearchParametersBuilder.forClass;

import java.util.Collection;

import net.cloudengine.dao.jpa.ResourceRepository;
import net.cloudengine.dao.jpa.ResourceTypeRepository;
import net.cloudengine.dao.support.Page;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.dao.support.SearchParametersBuilder;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.model.resource.Resource;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.model.resource.WayPoint;
import net.cloudengine.service.ResourceService;
import net.cloudengine.service.SqlService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

	private static final String RESOURCE_SEQ = "RESOURCE_SEQ";
	private ResourceTypeRepository typeDao;
	private ResourceRepository dao;
	private Repository<FileDescriptor, ObjectId> fileRepository;
	private Repository<WayPoint, ObjectId> wayPointRepository;
	private SqlService sqlService;
	
	@Autowired
	public ResourceServiceImpl(ResourceTypeRepository typeDao, ResourceRepository dao,
			RepositoryLocator repositoryLocator,
			SqlService sqlService) {
		this.typeDao = typeDao;
		this.fileRepository = repositoryLocator.getRepository(FileDescriptor.class);
		this.wayPointRepository = repositoryLocator.getRepository(WayPoint.class);
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
	public Page<Resource> getAll(int page, int size) {
		return dao.list(page, size);
	}
	
	@Override
	public Collection<Resource> getAll(long version) {
		return dao.getResourceWithGreaterVersion(version);
	}

	public Resource getByImei(String imei) {
		SearchParametersBuilder builder = SearchParametersBuilder.forClass(Resource.class);
		builder.eq("imei", imei);
		return dao.findOne(builder.build());
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
	public Page<ResourceType> getAllTypes(int page, int size) {
		return typeDao.list(page, size);
	}
	
	@Override
	public Collection<ResourceType> getAllTypes() {
		return typeDao.getAll();
	}

	@Override
	public Collection<FileDescriptor> getAllImages() {
		SearchParametersBuilder builder = forClass(FileDescriptor.class);
		builder.eq("type", "png");
		return fileRepository.findAll(builder.build());
	}

	@Override
	public FileDescriptor getImage(ObjectId id) {
		return fileRepository.get(id);
	}

	@Override
	public void save(WayPoint wayPoint) {
		wayPointRepository.save(wayPoint);
	}

}
