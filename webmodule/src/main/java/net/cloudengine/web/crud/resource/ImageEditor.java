package net.cloudengine.web.crud.resource;

import java.beans.PropertyEditorSupport;
import java.io.ByteArrayOutputStream;

import net.cloudengine.api.BlobStore;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.service.admin.ResourceService;

import org.bson.types.ObjectId;

public class ImageEditor extends PropertyEditorSupport {

	private ResourceService service;
	private BlobStore blobStore;

	public ImageEditor(ResourceService service, BlobStore blobStore) {
		this.service = service;
		this.blobStore = blobStore;
	}

	@Override
	public void setAsText(String text) {
		ObjectId id = new ObjectId(text);
		FileDescriptor descriptor = service.getImage(id);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		blobStore.retrieveFile(new ObjectId(descriptor.getFileId()), os);
		setValue(os.toByteArray());
	}

	@Override
	public String getAsText() {
		return "";
	}

}
