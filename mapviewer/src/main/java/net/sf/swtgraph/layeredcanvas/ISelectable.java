package net.sf.swtgraph.layeredcanvas;

/**
 * Esta interface debe ser implementada por todos los objetos
 * que pueden ser seleccionados en el mapa.
 * @author German Ulrich
 *
 */
public interface ISelectable {
	
	void setSelected(boolean selected);
	double getLon();
	double getLat();

}
