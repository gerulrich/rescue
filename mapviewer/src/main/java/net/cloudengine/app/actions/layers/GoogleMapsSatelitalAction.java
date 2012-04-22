package net.cloudengine.app.actions.layers;

import net.cloudengine.app.Application;
import net.cloudengine.mapviewer.MapWidget;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * This action class determines whether to show the book count
 */

public class GoogleMapsSatelitalAction extends Action {
	
	private static final int INDEX = 1;
	
	private Application app;
	
	
	public GoogleMapsSatelitalAction(Application app) {
		super("Google Maps (Satelital)", IAction.AS_RADIO_BUTTON);
		setChecked(false);
		this.app = app;
	}

	@Override
	public void run() {
		MapWidget mapWidget = app.getMapWidget();
		mapWidget.setTileServer(mapWidget.getServers()[INDEX]);
		mapWidget.redraw();
	}
	
}