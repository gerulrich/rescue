package net.cloudengine.app.actions.layers;

import net.cloudengine.app.Application;
import net.cloudengine.mapviewer.MapWidget;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * This action class determines whether to show the book count
 */

public class GoogleMapsStreetAction extends Action {
	
	private static final int INDEX = 0;
	
	private Application app;
	
	public GoogleMapsStreetAction(Application app) {
		super("Google Maps (Street)", IAction.AS_RADIO_BUTTON);
		setChecked(true);
		this.app = app;
	}
	
	@Override
	public void run() {
		MapWidget mapWidget = app.getMapWidget();
		mapWidget.setTileServer(mapWidget.getServers()[INDEX]);
		mapWidget.redraw();
	}
}