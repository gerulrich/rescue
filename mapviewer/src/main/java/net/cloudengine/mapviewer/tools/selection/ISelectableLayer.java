package net.cloudengine.mapviewer.tools.selection;

import java.util.Collection;

import net.sf.swtgraph.layeredcanvas.ICanvasLayer;
import net.sf.swtgraph.layeredcanvas.ISelectable;

/**
 * Interfaz para los layers que contienen elementos
 * que pueden ser seleccionados. Un {@link ICanvasLayer} que implemente
 * esta interface se invocarán los metodos de selección automaticamente
 * segun los eventos en el mapa.
 * @author German Ulrich
 *
 */
public interface ISelectableLayer {
	
	Group selectObjects(BoundingBox box, boolean keepSelection);
	
	Collection<ISelectable> getObjectsSelected();

}
