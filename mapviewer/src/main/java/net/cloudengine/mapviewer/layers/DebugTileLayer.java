package net.cloudengine.mapviewer.layers;

import net.cloudengine.mapviewer.MapWidget;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class DebugTileLayer implements ICanvasLayer {

	//FIXME sacar como constante a una clase utilitaria.
	private static final int TILE_SIZE = 256;
	private MapWidget map;
	
	public DebugTileLayer (MapWidget map) {
		this.map = map;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void paint(GC gc) {      Point size = map.getSize();
    int width = size.x, height = size.y;
    int x0 = (int) Math.floor(((double) map.mapPosition.x) / TILE_SIZE);
    int y0 = (int) Math.floor(((double) map.mapPosition.y) / TILE_SIZE);
    int x1 = (int) Math.ceil(((double) map.mapPosition.x + width) / TILE_SIZE);
    int y1 = (int) Math.ceil(((double) map.mapPosition.y + height) / TILE_SIZE);

    int dy = y0 * TILE_SIZE - map.mapPosition.y;
    for (int y = y0; y < y1; ++y) {
        int dx = x0 * TILE_SIZE - map.mapPosition.x;
        for (int x = x0; x < x1; ++x) {
            paintTile(gc, dx, dy, x, y);
            dx += TILE_SIZE;
//            ++getStats().tileCount;
        }
        dy += TILE_SIZE;
    }
    
//    long t1 = System.currentTimeMillis();
//    stats.dt = t1 - t0;
    //gc.drawString("dis ya draw", 20, 50);
	}
	

	private void paintTile(GC gc, int dx, int dy, int x, int y) {
		Display display = map.getDisplay();
		boolean DRAW_IMAGES = false;
		boolean DEBUG = true;
		boolean DRAW_OUT_OF_BOUNDS = false;

	    boolean imageDrawn = false;
	    int xTileCount = 1 << map.getZoom();
	    int yTileCount = 1 << map.getZoom();
	    boolean tileInBounds = x >= 0 && x < xTileCount && y >= 0 && y < yTileCount;
	    boolean drawImage = DRAW_IMAGES && tileInBounds;

	    if (DEBUG && (!imageDrawn && (tileInBounds || DRAW_OUT_OF_BOUNDS))) {
	    	gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
	    	gc.drawRectangle(dx + 4, dy + 4, TILE_SIZE - 8, TILE_SIZE - 8);
	    	String s = "T " + x + ", " + y + (!tileInBounds ? " #" : "");
	    	
	    	Color green = new Color (display, 93, 219, 49);
	    	gc.setBackground(green);
	    	gc.drawString(s, dx + 4+ 8, dy + 4 + 12);
	    	green.dispose();
	    }  else if (!DEBUG && !imageDrawn && tileInBounds) {
	    	
//	    	gc.setBackground(waitBackground);
//	    	gc.fillRectangle(dx, dy, TILE_SIZE, TILE_SIZE);
//	    	gc.setForeground(waitForeground);
//	    	for (int yl = 0; yl < TILE_SIZE; yl += 32) {
//	    		gc.drawLine(dx, dy + yl, dx + TILE_SIZE, dy + yl);
//	    	}
//	    	for (int xl = 0; xl < TILE_SIZE; xl += 32) {
//	    		gc.drawLine(dx + xl, dy, dx + xl, dy + TILE_SIZE);
//	    	}
	    }
	}

}
