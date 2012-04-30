package net.cloudengine.client.resources;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

public class ImgBundle {

	private ImageRegistry reg = new ImageRegistry();
	private static ImgBundle bundle;

	public static ImgBundle getImgBundle() {
		if (bundle != null)
			return bundle;
		bundle = new ImgBundle();
		return bundle;
	}

	public ImageDescriptor getImageDescriptor(String fileName) {
		fileName = fileName.toLowerCase();
		ImageDescriptor r = reg.getDescriptor(fileName);
		if (r != null)
			return r;
		r = ImageDescriptor.createFromURL(this.getClass().getResource("/images/"+fileName));
		reg.put(fileName, r);

		return r;
	}
	
	public void dispose() {
		reg.dispose();
		bundle = null;
	}	
}