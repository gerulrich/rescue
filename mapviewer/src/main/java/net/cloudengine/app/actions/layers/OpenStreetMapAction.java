package net.cloudengine.app.actions.layers;

import net.cloudengine.app.Application;
import net.cloudengine.mapviewer.MapWidget;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * This action class determines whether to show the book count
 */

public class OpenStreetMapAction extends Action {
	
	private static final int INDEX = 2;
	
	private Application app;
	
	public OpenStreetMapAction(Application app) {
		super("Open Street Map", IAction.AS_RADIO_BUTTON);
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