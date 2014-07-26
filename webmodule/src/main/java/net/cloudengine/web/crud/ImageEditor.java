package net.cloudengine.web.crud;

import java.beans.PropertyEditorSupport;
import java.io.ByteArrayOutputStream;

import net.cloudengine.dao.support.BlobStore;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.commons.FileDescriptor;

import org.bson.types.ObjectId;

public class ImageEditor extends PropertyEditorSupport {

	private Repository<FileDescriptor,ObjectId> repository;
	private BlobStore blobStore;

	public ImageEditor(RepositoryLocator repositoryLocator) {
		this.repository = repositoryLocator.getRepository(FileDescriptor.class);
		this.blobStore = repositoryLocator.getBlobStore();
	}

	@Override
	public void setAsText(String text) {
		ObjectId id = new ObjectId(text);
		FileDescriptor descriptor = repository.get(id);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		blobStore.retrieveFile(new ObjectId(descriptor.getFileId()), os);
		setValue(os.toByteArray());
	}

	@Override
	public String getAsText() {
		return "";
	}

}
