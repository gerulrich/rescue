package net.sf.swtgraph.layeredcanvas;

import java.util.Collection;

/**
 * Interfaz para los layers que contienen elementos
 * que pueden ser seleccionados. Un {@link ICanvasLayer} que implemente
 * esta interface se invocarán los metodos de selección automaticamente
 * segun los eventos en el mapa.
 * @author German Ulrich
 *
 */
public interface ISelectableLayer {
	
	void selectObjects(int x, int y);
	
	Collection<ISelectable> getObjectsSelected();

}
