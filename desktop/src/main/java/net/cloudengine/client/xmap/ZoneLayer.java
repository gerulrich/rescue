package net.cloudengine.client.xmap;

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.Datasource;
import net.cloudengine.mapviewer.layers.Item;
import net.cloudengine.mapviewer.layers.LayerDSBased;
import net.cloudengine.mapviewer.layers.Symbolizer;
import net.cloudengine.mapviewer.tools.selection.BoundingBox;
import net.cloudengine.mapviewer.tools.selection.Group;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ZoneLayer extends LayerDSBased<ZoneItem>  {

	public ZoneLayer(
			String name, MapWidgetContext context, 
			Datasource<ZoneItem> datasource,
			Symbolizer<ZoneItem> symbolizer,			
			boolean enabled) {
		super(name, context, datasource, symbolizer, enabled);
	}
	

	@Override
	public Group selectObjects(BoundingBox box, boolean keepSelection) {
		GeometryFactory factory = new GeometryFactory();
		Geometry bg = factory.toGeometry(box.getEnvelope());
		
		Group g = new Group(this.getName());
		List<Item> items = new ArrayList<Item>();
		
		for(ZoneItem item : this.datasource.getItems()) {
			if (bg.intersects(item.getGeom())) {
				item.selectItem();
				items.add(item);
			} else {
				if (keepSelection && item.isSelected()) {
					item.selectItem();
					items.add(item);
				} else {
					item.unselectItem();
				}
			}
		}
		g.setItems(items);
		return g;
	}
}
