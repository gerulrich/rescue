package net.cloudengine.api.mongo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import net.cloudengine.api.BlobStore;
import net.cloudengine.model.commons.FileDescriptor;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoBlobStore implements BlobStore {

	private static Logger logger = LoggerFactory.getLogger(MongoBlobStore.class);
	private MongoStore<FileDescriptor, ObjectId> fileStore;
	private GridFS gridfs;
	
	@Autowired
	public MongoBlobStore(MongoDBWrapper wrapper, @Qualifier("fileStore") MongoStore<FileDescriptor, ObjectId> fileStore) {
		super();
		this.gridfs = new GridFS(wrapper.getFactory().getDb());
		this.fileStore = fileStore;
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
	    	fileStore.save(fileUploaded);
	    	
	    	
	    } catch (Exception e) {
	    	logger.error("Error al guardar el archivo en el BlobStore", e);
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
