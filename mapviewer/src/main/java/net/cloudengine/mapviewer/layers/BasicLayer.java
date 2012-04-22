package net.cloudengine.mapviewer.layers;

import java.util.Collection;

import net.cloudengine.mapviewer.MapWidget;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;
import net.sf.swtgraph.layeredcanvas.ISelectable;
import net.sf.swtgraph.layeredcanvas.ISelectableLayer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class BasicLayer implements ICanvasLayer, ISelectableLayer {

//	private Image image_16 = new Image(Display.getCurrent(), BasicLayer.class.getResourceAsStream("police_16.png"));
	private Image image_24 = new Image(Display.getCurrent(), BasicLayer.class.getResourceAsStream("flag_green.png"));
//	private Image image_32 = new Image(Display.getCurrent(), BasicLayer.class.getResourceAsStream("police_32.png"));
//	private Image image_48 = new Image(Display.getCurrent(), BasicLayer.class.getResourceAsStream("police_48.png"));
		
	private MapWidget map;
	private boolean selected = false;
	int difx = 0;
	int dify = 0;
	
	public BasicLayer(MapWidget map) {
		this.map = map;
	}
	
	public void dispose() {
//		image_16.dispose();
		image_24.dispose();
//		image_32.dispose();
//		image_48.dispose();
	}

	public void paint(GC gc) {
		
		Device device = Display.getCurrent();
		Color green = new Color (device, 93, 219, 49);

//		int difx = MapWidget.lon2position(-58.51210, map.getZoom()) -map.mapPosition.x;
//		int dify = MapWidget.lat2position(-34.52596, map.getZoom()) -map.mapPosition.y;
		
		difx = MapWidget.lon2position(-58.36798, map.getZoom()) -map.mapPosition.x;
		dify = MapWidget.lat2position(-34.61761, map.getZoom()) -map.mapPosition.y;
		
		Color currentBc = gc.getBackground();
		Color currentFc = gc.getForeground();
		
		if (selected) {
			gc.setAntialias(SWT.ON);
			gc.setLineStyle(1);
			gc.setLineWidth(2);
			gc.setForeground(green);
			gc.setBackground(green);
		
			gc.drawArc(difx-15, dify-8, 30, 16, 0, 360);
		
			int alpha = gc.getAlpha();
			gc.setAlpha(50);
			gc.fillArc(difx-15, dify-8, 30, 16, 0, 360);
			gc.setAlpha(alpha);
		}		
		
		gc.drawImage(image_24, 0, 0, image_24.getImageData().width, image_24.getImageData().height, difx-image_24.getImageData().width/4+2, dify-image_24.getImageData().height+2, image_24.getImageData().width, image_24.getImageData().height);
//		gc.drawPoint(difx, dify);
		
		
		gc.setBackground(currentBc);
		gc.setForeground(currentFc);
		green.dispose();
	}

	@Override
	public void selectObjects(int x, int y) {
		if (difx >= x-10 && difx <= x+10 && dify >=y-10 && dify <= y+10) {
			selected = true;
		} else {
			selected = false;
		}
		System.out.println("x="+x+", y="+y);
		System.out.println("objeto x="+difx+", y="+dify);
	}

	@Override
	public Collection<ISelectable> getObjectsSelected() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
