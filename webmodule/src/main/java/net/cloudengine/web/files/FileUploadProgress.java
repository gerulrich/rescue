package net.cloudengine.web.files;

public class FileUploadProgress {

	private long uploaded;
	private long total;

	public FileUploadProgress(long uploaded, long total) {
		super();
		this.uploaded = uploaded;
		this.total = total;
	}

	public long getUploaded() {
		return uploaded;
	}

	public void setUploaded(long uploaded) {
		this.uploaded = uploaded;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}