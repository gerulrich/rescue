package net.cloudengine.mapviewer.tools.renderer;

import org.eclipse.swt.graphics.GC;

public interface Renderer {
	
	void paint(GC gc);
	void clear();
	void dispose();

}
