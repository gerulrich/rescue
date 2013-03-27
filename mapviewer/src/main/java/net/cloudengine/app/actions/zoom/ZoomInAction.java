package net.cloudengine.app.actions.zoom;

import net.cloudengine.app.MapApplication;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Point;

public class ZoomInAction extends Action {
	
	private MapApplication app;
	
	public ZoomInAction(MapApplication app) {
		super("Acercar zoom", ImageDescriptor.createFromURL(ZoomInAction.class.getResource("zoom_in.png")));
		this.app = app;
	}

	@Override
	public void run() {
		if (app.getMapWidget() != null) {
			Point p = app.getMapWidget().getSize();		
			app.getMapWidget().zoomIn(new Point(p.x/2,p.y/2));
		}
	}
}
