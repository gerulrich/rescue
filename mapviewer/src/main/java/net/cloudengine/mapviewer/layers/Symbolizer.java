package net.cloudengine.mapviewer.layers;

import net.cloudengine.mapviewer.MapWidgetContext;

import org.eclipse.swt.graphics.GC;

public interface Symbolizer<T extends Item> {
	
	
	void symbolize(GC gc, T item, MapWidgetContext context, int zoom);

}
