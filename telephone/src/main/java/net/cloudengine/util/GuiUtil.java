package net.cloudengine.util;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

public class GuiUtil {

	private static final String IMAGE_DIR = "/images/";
	
	public static Image getImage(String imageName) {
		ImageRegistry registry = JFaceResources.getImageRegistry();
		Image image = registry.get(imageName); 
		if (image == null) {
			  registry.put(imageName, ImageDescriptor.createFromFile(GuiUtil.class, IMAGE_DIR+imageName));
			  image = registry.get(imageName);
		}
		return image;
	}
	
	
	public static Font getSavedFont (String fontName, int fontSize, int fontStyle) {
		String fontString = "FONT:" + fontName + "-" + fontSize + "-" + fontStyle;
		FontRegistry fontRegistry = JFaceResources.getFontRegistry();
		if (!fontRegistry.hasValueFor(fontString)) {
			fontRegistry.put(fontString, new FontData[]{ new FontData(fontName, fontSize, fontStyle) });
		}
		return fontRegistry.get(fontString);
	}
	
	public static Color getSavedColor (int r, int g, int b) {
		String colorString = "COLOR:" + r + "-" + g + "-" + b;
		ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
		if (!colorRegistry.hasValueFor(colorString)) {
			colorRegistry.put(colorString, new RGB(r, g, b));
		}
		return colorRegistry.get(colorString);
	}
	
	public static void changeImage(final Button b, final Image i) {
		if (Display.getDefault() == null) {
			return;
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!b.isDisposed()) {
					b.setImage(i);
				}
			}
		});
	}
	
}
