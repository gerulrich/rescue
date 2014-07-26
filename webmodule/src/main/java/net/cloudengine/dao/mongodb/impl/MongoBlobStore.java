package net.cloudengine.dao.mongodb.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import net.cloudengine.dao.support.BlobStore;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.service.amqp.AMQPService;
import net.cloudengine.service.amqp.EndPoint;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoBlobStore implements BlobStore {

	private static Logger logger = LoggerFactory.getLogger(MongoBlobStore.class);
	private Repository<FileDescriptor, ObjectId> fileRepository;
	private GridFS gridfs;
	private AMQPService amqpService;
	
	@Autowired
	public MongoBlobStore(MongoTemplate mongoTemplate, RepositoryLocator repositoryLocator, AMQPService amqpService) {
		super();
		this.gridfs = new GridFS(mongoTemplate.getDb());
		this.fileRepository = repositoryLocator.getRepository(FileDescriptor.class);
		this.amqpService = amqpService;
	}
	
	@Override
	public void remove(ObjectId id) {
		gridfs.remove(id);
	}

	@Override
	public boolean exists(String filename) {
		if (logger.isDebugEnabled()) {
			logger.debug("Verificando si existe el archivo {} en el BlobStore", filename);
		}
		return gridfs.findOne(filename) != null;
	}

	@Override
	public void storeFile(String filename, InputStream inputStream, String description, String type, String version) {
		if (logger.isDebugEnabled()) {
			logger.debug("Guardando el archivo {} en el BlobStore", filename);
		}
		GridFSInputFile inputFile = gridfs.createFile(inputStream);
	    inputFile.setContentType(type);
	    inputFile.setFilename(filename);
	    inputFile.save();
	    try {
	    	inputStream.close();
	    	
	    	FileDescriptor fileUploaded = new FileDescriptor();
	    	fileUploaded.setFilename(filename);
	    	fileUploaded.setType(type);
	    	fileUploaded.setDescription(description);
	    	fileUploaded.setFileId(inputFile.getId().toString());
	    	fileUploaded.setSize(inputFile.getLength());
	    	fileUploaded.setDate(new Date());
	    	fileUploaded.setVersion(version);
	    	fileRepository.save(fileUploaded);
	    	sendMessage(fileUploaded.getFileId(), type);
	    } catch (Exception e) {
	    	logger.error("Error al guardar el archivo en el BlobStore", e);
	    }
	}
	
	private void sendMessage(String fileid, String type) {
		String message = String.format("{\"id\": \"%s\"}", fileid);
		if ("rpt".equals(type)) {
			amqpService.send(message, EndPoint.REPORT_UPLOAD);
		} else if ("wf".equals(type)) {
			amqpService.send(message, EndPoint.WORKFLOW_UPLOAD);
		}
	}

	@Override
	public void retrieveFile(ObjectId id, OutputStream outputStream) {
		if (logger.isDebugEnabled()) {
			logger.debug("Obteniendo el archivo con id {} del BlobStore", id.toString());
		}
		GridFSDBFile dbfile = gridfs.findOne(id);
	    try {
	    	dbfile.writeTo(outputStream);
	    } catch (Exception e) {
	    	logger.error("Error al obtener el archivo con id {} del BlobStore", id.toString(), e);
	    } finally {
	    	IOUtils.closeQuietly(outputStream);
	    }
	}
	
	@Override
	public InputStream retrieveFile(ObjectId id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Obteniendo el archivo con id {} del BlobStore", id.toString());
		}
		GridFSDBFile dbfile = gridfs.findOne(id);
		return dbfile.getInputStream();
	}

}
