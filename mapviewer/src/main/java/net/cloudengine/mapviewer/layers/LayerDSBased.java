package net.cloudengine.mapviewer.layers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.tools.selection.BoundingBox;
import net.cloudengine.mapviewer.tools.selection.Group;
import net.cloudengine.mapviewer.tools.selection.ISelectableLayer;
import net.sf.swtgraph.layeredcanvas.AbstractLayer;
import net.sf.swtgraph.layeredcanvas.ISelectable;

import org.eclipse.swt.graphics.GC;

public class LayerDSBased<T extends Item> extends AbstractLayer implements ISelectableLayer {

	protected Datasource<T> datasource;
	protected	Symbolizer<T> symbolizer;
	
	public LayerDSBased(
			String name, MapWidgetContext context, 
			Datasource<T> datasource,
			Symbolizer<T> symbolizer,			
			boolean enabled) {
		super(name, context, enabled);
		this.datasource = datasource;
		this.symbolizer = symbolizer;
		this.datasource.init(context);
	}

	@Override
	public void paint(GC gc) {
		for (T item : datasource.getItems()) {
			symbolizer.symbolize(gc, item, getContext(), getMap().getZoom());
		}
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public Group selectObjects(BoundingBox bbox, boolean keepSelection) {
		Group group = new Group(getName());
		List<Item> selectedItems = new ArrayList<Item>();
		for (T item : datasource.getItems()) {
			if (item.allowSelection()) {
				boolean selected = bbox.contains(item.getLon(),item.getLat());
				if (selected || (keepSelection && item.isSelected())) {
					item.selectItem();
					selectedItems.add(item);
				} else {
					item.unselectItem();
				}
			}
		}
		group.setItems(selectedItems);
		return group;
	}

	@Override
	public Collection<ISelectable> getObjectsSelected() {

		return null;
	}

}
