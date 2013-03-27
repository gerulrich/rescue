package net.cloudengine.mapviewer.tools.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.Item;
import net.cloudengine.mapviewer.tools.AbstractTool;
import net.cloudengine.mapviewer.tools.renderer.RendererLayer;
import net.cloudengine.mapviewer.util.GeoLocation;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

public class SelectionTool extends AbstractTool {

	private SelectionMouseListener listener;
	private SelectionRenderer renderer;
    private Cursor cursor;

	
	public SelectionTool(MapWidgetContext context) {
		super(context, false);
		this.listener = new SelectionMouseListener(context, this);
		cursor = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);
		renderer = new SelectionRenderer();
	}

	@Override
	public void activate() {
		this.getMap().setCursor(cursor);
		getContext().getMap().addMouseListener(listener);
		getContext().getMap().addMouseMoveListener(listener);
		RendererLayer layer = getContext().getMap().getLayer(RendererLayer.class);
		if (layer != null) {
			layer.addRenderer("SELECTON", renderer);
		}
	}

	@Override
	public void deactivate() {
		getContext().getMap().removeMouseListener(listener);
		getContext().getMap().removeMouseMoveListener(listener);
		RendererLayer layer = getContext().getMap().getLayer(RendererLayer.class);
		if (layer != null) {
			layer.removeRenderer("SELECTON");
		}
		getContext().getMap().redraw();
	}

	@Override
	public ImageDescriptor getIcon() {
		return ImageDescriptor.createFromURL(SelectionTool.class.getResource("arrow-cursor.png"));
	}
	
	
	public void clearSelection() {
		for(Group group: this.getContext().getGroups()) {
			for(Item item : group.getItems()) {
				item.unselectItem();
			}
		}
		getContext().setGroups(new ArrayList<Group>());
		getContext().setGroups(Collections.<Group>emptyList());
		getContext().getMap().redraw();
	}
	
	
	public void selectItems(double lon1, double lat1, double lon2, double lat2, boolean keepSelection) {
		
		BoundingBox bb = new BoundingBox(lon1,lat1,lon2,lat2);
		
		List<Group> groups = new ArrayList<Group>();
		for(ICanvasLayer layer:  getContext().getMap().getLayers()) {
    		if (layer instanceof ISelectableLayer && layer.isEnabled()) {
    			ISelectableLayer selectableLayer = ((ISelectableLayer)layer);
    			Group group = selectableLayer.selectObjects(bb, keepSelection);
    			if (!group.isEmpty()) {
    				groups.add(group);
    			}
    		}
    	}
		getContext().setGroups(groups);
		getContext().getMap().redraw();
		
	}
	
	public void selectItems(double lon, double lat, boolean keepSelection) {
		
		int zoom = getContext().getMap().getZoom();
		
		// segun el nivel de zoom cambio agrando o disminuyo el area de seleccion.
		double distance = 15;
		if (zoom < 14) {
			distance = 100;
		} else if (zoom < 16) {
			distance = 50;
		}
		
		GeoLocation location = GeoLocation.fromDegrees(lon,lat);
		GeoLocation bbox[] = location.boundingCoordinates(distance, 6378136.6f);
		
		BoundingBox bb = new BoundingBox(
				bbox[0].getLongitudeInDegrees(),
				bbox[0].getLatitudeInDegrees(),
				bbox[1].getLongitudeInDegrees(),
				bbox[1].getLatitudeInDegrees()
		);
		
		List<Group> groups = new ArrayList<Group>();
		for(ICanvasLayer layer:  getContext().getMap().getLayers()) {
    		if (layer instanceof ISelectableLayer  && layer.isEnabled()) {
    			Group group = ((ISelectableLayer)layer).selectObjects(bb, keepSelection);
    			if (!group.isEmpty()) {
    				groups.add(group);
    			}
    		}
    	}
		getContext().setGroups(groups);
		getContext().getMap().redraw();
		
	}

	public SelectionRenderer getRenderer() {
		return renderer;
	}

}
