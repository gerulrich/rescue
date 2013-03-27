package net.cloudengine.mapviewer.layers;

import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.util.MapConstants;
import net.sf.swtgraph.layeredcanvas.AbstractLayer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class DebugTileLayer extends AbstractLayer {

	public DebugTileLayer(MapWidgetContext context) {
		super("Debug", context, false);
	}
	
	@Override
	public void dispose() {
		// NADA
	}

	@Override
	public void paint(GC gc) {      
		MapWidget map = getMap();
		Point size = map.getSize();
		int width = size.x, height = size.y;
		int x0 = (int) Math.floor(((double) map.mapPosition.x) / MapConstants.TILE_SIZE);
		int y0 = (int) Math.floor(((double) map.mapPosition.y) / MapConstants.TILE_SIZE);
		int x1 = (int) Math.ceil(((double) map.mapPosition.x + width) / MapConstants.TILE_SIZE);
		int y1 = (int) Math.ceil(((double) map.mapPosition.y + height) / MapConstants.TILE_SIZE);

		int dy = y0 * MapConstants.TILE_SIZE - map.mapPosition.y;
		for (int y = y0; y < y1; ++y) {
			int dx = x0 * MapConstants.TILE_SIZE - map.mapPosition.x;
			for (int x = x0; x < x1; ++x) {
				paintTile(gc, dx, dy, x, y);
				dx += MapConstants.TILE_SIZE;
			}
			dy += MapConstants.TILE_SIZE;
		}
	}
	

	private void paintTile(GC gc, int dx, int dy, int x, int y) {
		MapWidget map = getMap();
		Display display = map.getDisplay();
//		boolean DRAW_IMAGES = false;
		boolean DEBUG = true;
		boolean DRAW_OUT_OF_BOUNDS = false;

	    boolean imageDrawn = false;
	    int xTileCount = 1 << map.getZoom();
	    int yTileCount = 1 << map.getZoom();
	    boolean tileInBounds = x >= 0 && x < xTileCount && y >= 0 && y < yTileCount;
//	    boolean drawImage = DRAW_IMAGES && tileInBounds;

	    if (DEBUG && (!imageDrawn && (tileInBounds || DRAW_OUT_OF_BOUNDS))) {
	    	gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
	    	gc.drawRectangle(dx + 4, dy + 4, MapConstants.TILE_SIZE - 8, MapConstants.TILE_SIZE - 8);
	    	String s = "T " + x + ", " + y + (!tileInBounds ? " #" : "");
	    	
	    	Color green = new Color (display, 93, 219, 49);
	    	gc.setBackground(green);
	    	gc.drawString(s, dx + 4+ 8, dy + 4 + 12);
	    	green.dispose();
	    }
	}
}
