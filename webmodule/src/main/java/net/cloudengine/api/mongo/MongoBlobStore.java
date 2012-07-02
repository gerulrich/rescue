package net.cloudengine.api.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.cloudengine.api.BlobStore;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.web.MongoDBWrapper;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.document.mongodb.SimpleMongoDbFactory;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoBlobStore implements BlobStore {

	private SimpleMongoDbFactory factory;
	private GridFS gridfs;
	private MongoStore<FileDescriptor, ObjectId> fileStore;
	
	@Autowired
	public MongoBlobStore(MongoDBWrapper wrapper, @Qualifier("fileStore") MongoStore<FileDescriptor, ObjectId> fileStore) {
		super();
		this.factory = wrapper.getFactory();
		this.gridfs = new GridFS(factory.getDb());
		this.fileStore = fileStore;
	}
	
	@Override
	public void remove(ObjectId id) {
		gridfs.remove(id);
	}

	@Override
	public boolean exists(String filename) {
		return gridfs.findOne(filename) != null;
	}

	@Override
	public List<String> list() {
		List<String> fileList = new ArrayList<String>();
		DBCursor cursor = gridfs.getFileList();
		while (cursor.hasNext()) {
			// TODO
		}
		return fileList;
	}

	@Override
	public void storeFile(String filename, InputStream inputStream, String description, String type) {
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
	    	fileStore.save(fileUploaded);
	    	
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	@Override
	public void retrieveFile(ObjectId id, OutputStream outputStream) {
	    GridFSDBFile dbfile = gridfs.findOne(id);
	    try {
	    	dbfile.writeTo(outputStream);
	    } catch (Exception e) {
	    	
	    } finally {
	    	try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

}
