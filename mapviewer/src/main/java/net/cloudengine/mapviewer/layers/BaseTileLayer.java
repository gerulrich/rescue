package net.cloudengine.mapviewer.layers;

import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.tiles.AsyncImage;
import net.cloudengine.mapviewer.tiles.TileCache;
import net.cloudengine.mapviewer.tiles.TileServer;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class BaseTileLayer implements ICanvasLayer {

	//FIXME sacar como constante a una clase utilitaria.
	private static final int TILE_SIZE = 256;
	private Color waitBackground;
	private Color waitForeground;
	private MapWidget map;
	
	public BaseTileLayer(MapWidget map) {
		this.map = map;
		waitBackground = new Color(Display.getCurrent(), 0x88, 0x88, 0x88);
        waitForeground = new Color(Display.getCurrent(), 0x77, 0x77, 0x77);
	}
	public void dispose() {
		waitBackground.dispose();
		waitForeground.dispose();
	}

	public void paint(GC gc) {
      map.getStats().reset();
      long t0 = System.currentTimeMillis();
		
      Point size = map.getSize();
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
              map.getStats().incTileCount();
          }
          dy += TILE_SIZE;
      }
      
      long t1 = System.currentTimeMillis();
      map.getStats().setDt(t1 - t0);
	}
	

	private void paintTile(GC gc, int dx, int dy, int x, int y) {
		Display display = map.getDisplay();
		boolean DRAW_IMAGES = true;
		boolean DEBUG = false;
		boolean DRAW_OUT_OF_BOUNDS = false;

	    boolean imageDrawn = false;
	    int xTileCount = 1 << map.getZoom();
	    int yTileCount = 1 << map.getZoom();
	    boolean tileInBounds = x >= 0 && x < xTileCount && y >= 0 && y < yTileCount;
	    boolean drawImage = DRAW_IMAGES && tileInBounds;
	    if (drawImage) {
	    	TileCache cache = map.getCache();
	    	TileServer tileServer = map.getTileServer();
	    	AsyncImage image = cache.get(tileServer, x, y, map.getZoom());
	    	if (image == null) {
	    		image = new AsyncImage(map, tileServer, x, y, map.getZoom());
	    		cache.put(tileServer, x, y, map.getZoom(), image);
	    	}
	        if (image.getImage(map.getDisplay()) != null) {
	        	gc.drawImage(image.getImage(map.getDisplay()), dx, dy);
	            imageDrawn = true;
	        }
	    }
	    if (DEBUG && (!imageDrawn && (tileInBounds || DRAW_OUT_OF_BOUNDS))) {
	    	gc.setBackground(display.getSystemColor(tileInBounds ? SWT.COLOR_GREEN : SWT.COLOR_RED));
	    	gc.fillRectangle(dx + 4, dy + 4, TILE_SIZE - 8, TILE_SIZE - 8);
	    	gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
	    	String s = "T " + x + ", " + y + (!tileInBounds ? " #" : "");
	    	gc.drawString(s, dx + 4+ 8, dy + 4 + 12);
	    }  else if (!DEBUG && !imageDrawn && tileInBounds) {
	    	gc.setBackground(waitBackground);
	    	gc.fillRectangle(dx, dy, TILE_SIZE, TILE_SIZE);
	    	gc.setForeground(waitForeground);
	    	for (int yl = 0; yl < TILE_SIZE; yl += 32) {
	    		gc.drawLine(dx, dy + yl, dx + TILE_SIZE, dy + yl);
	    	}
	    	for (int xl = 0; xl < TILE_SIZE; xl += 32) {
	    		gc.drawLine(dx + xl, dy, dx + xl, dy + TILE_SIZE);
	    	}
	    }
	}
	
	//---------
	@Override
	public void setEnabled(boolean enabled) { }
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String getName() {
		return "Capa Base";
	}
	
	@Override
	public boolean showLegend() {
		return true;
	}	
	
}