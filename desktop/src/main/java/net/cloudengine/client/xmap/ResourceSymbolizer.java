package net.cloudengine.client.xmap;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.cloudengine.client.ui.AnnotatedCallbackResolver;
import net.cloudengine.client.ui.JobUtils;
import net.cloudengine.client.ui.PostCallback;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.Symbolizer;
import net.cloudengine.mapviewer.util.MapUtil;
import net.cloudengine.rpc.controller.resource.ResourceController;
import net.cloudengine.rpc.model.resource.ResourceTypeModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ResourceSymbolizer implements Symbolizer<ResourceItem> {

	private ConcurrentMap<Long, Image> images = new ConcurrentHashMap<Long, Image>();
	Color selectionColor;
	
	public ResourceSymbolizer(ResourceController controller) {
		super();
		this.selectionColor = new Color (Display.getCurrent(), 200, 44, 49);
		JobUtils.execAsync(controller, new AnnotatedCallbackResolver(this, "loadTypes")).getTypes();
	}
	
	@PostCallback(name="loadTypes")
	public void loadTypes(List<ResourceTypeModel> types) {
		for(ResourceTypeModel type : types) {
			Image image = new Image(Display.getCurrent(), new ByteArrayInputStream(type.getImage()));
			images.put(type.getId(), image);
		}
	}

	@Override
	public void symbolize(GC gc, ResourceItem item, MapWidgetContext context, int zoom) {
		int x = lon2position(item.getLon(), zoom, context);
		int y = lat2position(item.getLat(), zoom, context);
		
		Color currentBc = gc.getBackground();
		Color currentFc = gc.getForeground();
		
		if (item.isSelected()) {
			drawSelection(gc, x, y, selectionColor);
		}		
		
		Image image = images.get(item.getType());
		if (image != null) {
			int width = image.getImageData().width;
			int height = image.getImageData().height;
			gc.drawImage(image, 0, 0, width, height, x-width/2, y-height/2, width, height);
		}
		
		gc.setBackground(currentBc);
		gc.setForeground(currentFc);
	}
	
	
	private void drawSelection(GC gc, int x, int y, Color color) {
		gc.setAntialias(SWT.ON);
		gc.setLineStyle(1);
		gc.setLineWidth(2);
		gc.setForeground(color);
		gc.setBackground(color);
		gc.drawArc(x-30, y-5, 60, 30, 0, 360);
		int alpha = gc.getAlpha();
		gc.setAlpha(50);
		gc.fillArc(x-30, y-5, 60, 30, 0, 360);
		gc.setAlpha(alpha);
	}
	
	public void dispose() {
		for(Long id : images.keySet()) {
			Image image = images.remove(id);
			image.dispose();
		}
		this.selectionColor.dispose();
	}
	
	private int lon2position(double lon, int zoom, MapWidgetContext context) {
		return MapUtil.lon2position(lon, zoom) -context.getMap().mapPosition.x;
	}
	
	private int lat2position(double lat, int zoom, MapWidgetContext context) {
		return MapUtil.lat2position(lat, zoom) -context.getMap().mapPosition.y;
	}

}
