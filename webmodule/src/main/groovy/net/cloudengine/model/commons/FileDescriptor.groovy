package net.cloudengine.model.commons

import org.bson.types.ObjectId

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id

@Entity(value="file_descriptor", noClassnameStored=true)
class FileDescriptor {
	
	@Id
	ObjectId id;
	String fileId;
	String filename;
	String type;
	String description;
	String version;
	Date date;
	long size;
	
	String getSizeReadable() {
		int unit = 1024;
		if (size < unit) return size + " B";
		int exp = (int) (Math.log(size) / Math.log(unit));
		char pre = ("KMGTPE").charAt(exp-1);
		return String.format("%.1f %cB", size / Math.pow(unit, exp), pre);
	}
	
	ObjectId getFileObjectId() {
		return new ObjectId(fileId);
	}

}
