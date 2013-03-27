package net.cloudengine.mapviewer.layers;

import java.util.List;

import net.cloudengine.mapviewer.MapWidgetContext;

public interface Datasource<T extends Item> {

	void init(MapWidgetContext context);
	List<T> getItems();
	
}
