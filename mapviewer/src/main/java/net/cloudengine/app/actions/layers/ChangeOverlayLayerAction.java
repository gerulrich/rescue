package net.cloudengine.app.actions.layers;

import net.cloudengine.app.MapApplication;
import net.cloudengine.mapviewer.MapWidget;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class ChangeOverlayLayerAction extends Action {
	
	private MapApplication app;
	private ICanvasLayer layer;
	
	public ChangeOverlayLayerAction(String name, MapApplication app, ICanvasLayer layer) {
		super(name, IAction.AS_CHECK_BOX);
		setChecked(layer.isEnabled());
		this.app = app;
		this.layer = layer;
	}

	@Override
	public void run() {
		MapWidget mapWidget = app.getMapWidget();
		layer.setEnabled(this.isChecked());
		mapWidget.redraw();
	}
}
