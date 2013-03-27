package net.cloudengine.app.actions.zoom;

import net.cloudengine.app.MapApplication;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Point;

public class ZoomGeneralAction extends Action {
	
	
	private MapApplication app;
	
	public ZoomGeneralAction(MapApplication app) {
		super("&Zoom general", ImageDescriptor.createFromURL(ZoomOutAction.class.getResource("zoom_best_fit.png")));
		this.app = app;
	}

	@Override
	public void run() {
//		MapWidget md = Application.this.getMapWidget();
		app.getMapWidget().setZoom(12);
        Point position = new Point(354083, 631905); 
        app.getMapWidget().setCenterPosition(position);
        app.getMapWidget().redraw();
	}


}
