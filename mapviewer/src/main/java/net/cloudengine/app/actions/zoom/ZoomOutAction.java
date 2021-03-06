package net.cloudengine.app.actions.zoom;

import net.cloudengine.app.MapApplication;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Point;

public class ZoomOutAction extends Action {
	
	private MapApplication app;
	
	public ZoomOutAction(MapApplication app) {
		super("Alejar zoom", ImageDescriptor.createFromURL(ZoomOutAction.class.getResource("zoom_out.png")));
		this.app = app;
	}

	@Override
	public void run() {
		if (app.getMapWidget() != null) {
			Point p = app.getMapWidget().getSize();		
			app.getMapWidget().zoomOut(new Point(p.x/2,p.y/2));
		}
	}
}
