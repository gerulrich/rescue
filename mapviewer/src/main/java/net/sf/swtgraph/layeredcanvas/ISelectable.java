package net.sf.swtgraph.layeredcanvas;

import java.util.Collection;

public interface ISelectable {
	
	void selectObjects(int x, int y);
	
	Collection<Object> getObjectsSelected();

}
