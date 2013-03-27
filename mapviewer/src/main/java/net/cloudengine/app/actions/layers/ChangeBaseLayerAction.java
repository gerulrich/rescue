package net.cloudengine.app.actions.layers;

import net.cloudengine.app.MapApplication;
import net.cloudengine.mapviewer.MapWidget;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class ChangeBaseLayerAction extends Action {
	
	private MapApplication app;
	private int index;
	
	public ChangeBaseLayerAction(String name, MapApplication app, int index, boolean checked) {
		super(name, IAction.AS_RADIO_BUTTON);
		setChecked(checked);
		this.app = app;
		this.index = index;
	}

	@Override
	public void run() {
		MapWidget mapWidget = app.getMapWidget();
		mapWidget.setCurrentTileServer(mapWidget.getBaseTileServers().get(index));
		mapWidget.redraw();
	}
}
