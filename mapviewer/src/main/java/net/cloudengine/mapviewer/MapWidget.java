package net.cloudengine.mapviewer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import net.cloudengine.mapviewer.layers.BaseTileLayer;
import net.cloudengine.mapviewer.tiles.TileCache;
import net.cloudengine.mapviewer.tiles.TileServer;
import net.cloudengine.mapviewer.tools.AbstractTool;
import net.cloudengine.mapviewer.util.MapConstants;
import net.cloudengine.mapviewer.util.MapUtil;
import net.cloudengine.mapviewer.util.PointD;
import net.cloudengine.mapviewer.util.Stats;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;
import net.sf.swtgraph.layeredcanvas.LayeredCanvas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class MapWidget extends LayeredCanvas {
    
	private MapWidgetContext context;
	private Point mouseCoords = new Point(0, 0);
    public void setMouseCoords(Point mouseCoords) {
		Point oldValue = this.mouseCoords; 
    	this.mouseCoords = mouseCoords;
    	pcs.firePropertyChange("mouseCoords", oldValue, this.mouseCoords);
	}

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Point mapSize = new Point(0, 0);
    public Point mapPosition = new Point(0, 0);
    private int zoom;
    private AtomicLong zoomStamp = new AtomicLong();

    private TileCache cache = new TileCache();
    private Stats stats = new Stats();
//    private MapMouseListener mouseListener;
    
    private List<TileServer> baseTileServers = new ArrayList<TileServer>();
    private List<TileServer> overlayTileServers = new ArrayList<TileServer>();
    private TileServer currentTileServer = null;
    
    private List<AbstractTool> tools = new ArrayList<AbstractTool>();
    
    //-- Metodos nuevos --
    
    public void repaintMap() {
    	this.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				MapWidget.this.redraw();
			}
    	});
    }
    
    public <T extends AbstractTool> T getTool(Class<T> clazz) {
    	T tool = null;
    	for(AbstractTool t : tools) {
    		if (clazz.equals(t.getClass())) {
    			tool = (T)t;
    			break;
    		}
    	}
    	return tool;
    }
    
    public <T extends ICanvasLayer> T getLayer(Class<T> clazz) {
    	T layer = null;
    	for(ICanvasLayer l : context.getlayers()) {
    		if (clazz.equals(l.getClass())) {
    			layer = (T)l;
    			break;
    		}
    	}
    	return layer;
    }

    
    
    public void addTileServer(TileServer tileServer, boolean base) {
    	if (base) {
    		baseTileServers.add(tileServer);
    		if (currentTileServer == null) {
    			currentTileServer = tileServer;
    		}
    	} else {
    		overlayTileServers.add(tileServer);
    	}
    }
    
    public List<TileServer> getBaseTileServers() {
    	return baseTileServers;
    }
    
    public void addTool(AbstractTool tool) {
    	this.tools.add(tool);
    }
    
    //----------------------------
    
    
    public MapWidget(Composite parent, int style, MapWidgetContext context) {
        this(parent, style, new Point(354083, 631905), 12, context);
    }
    
    public MapWidget(Composite parent, int style, Point mapPosition, int zoom, MapWidgetContext context) {
        super(parent, SWT.DOUBLE_BUFFERED | style);
        this.context = context;
        context.setMap(this);
        
        baseTileServers.addAll(context.getBaseTileServers());
        currentTileServer =baseTileServers.get(0);
        
//        mouseListener = new MapMouseListener(context, new ZoomTool(context));
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
               MapWidget.this.widgetDisposed(e);
            }
        });

//        addMouseListener(mouseListener);
//        addMouseMoveListener(mouseListener);
//        addMouseWheelListener(mouseListener);
//        addMouseTrackListener(mouseListener);
        
//        this.addTool(new ZoomTool(context));
//        for(AbstractTool tool : this.tools) {
//        	tool.registerListeners(this);
//        }
          for(AbstractTool tool : context.getTools()) {
        	  addTool(tool);
          }
        
        this.addLayer(new BaseTileLayer(this));
        for(ICanvasLayer layer : context.getlayers()) {
        	this.addLayer(layer);
        }      
      
        setZoom(zoom);
//        setMapPosition(mapPosition);
        setCenterPosition(mapPosition);
        this.redraw();
      
    }
    
    public TileCache getCache() {
        return cache;
    }
    
    public TileServer getTileServer() {
        return currentTileServer;
    }
    
    public void setCurrentTileServer(TileServer tileServer) {
        this.currentTileServer = tileServer;
        redraw();
    }
    
    public Stats getStats() {
        return stats;
    }    
    
    
    protected void widgetDisposed(DisposeEvent e) {
        // TODO dispose de los layers
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
    
    public Point getMapPosition() {
        return new Point(mapPosition.x, mapPosition.y);
    }

    public void setMapPosition(Point mapPosition) {
        setMapPosition(mapPosition.x, mapPosition.y);
    }

    public void setMapPosition(int x, int y) {
        if (mapPosition.x == x && mapPosition.y == y)
            return;
        Point oldMapPosition = getMapPosition();
        mapPosition.x = x;
        mapPosition.y = y;
        pcs.firePropertyChange("mapPosition", oldMapPosition, getMapPosition());
    }
    
    public void translateMapPosition(int tx, int ty) {
        setMapPosition(mapPosition.x + tx, mapPosition.y + ty);
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        if (zoom == this.zoom)
            return;
        zoomStamp.incrementAndGet();
        // lets not go through the bizarre below and just do the line above
        // for cooperative cancellation
//        if (false) {
//            try {
//                Iterator<Runnable> it = executor.getQueue().iterator();
//                while (it.hasNext()) {
//                    Runnable r = it.next();
//                    if (r instanceof Future<?>) {
//                        Future<?> c = (Future<?>)r;
//                        c.cancel(true);
//                    }
//                }
//            } catch (ConcurrentModificationException e) {
//                log.log(Level.INFO, "concurrent modification of executor queue", e);
//            }
//            executor.purge();
//        }
        int oldZoom = this.zoom;
        this.zoom = Math.min(getTileServer().getMaxZoom(), zoom);
        mapSize.x = MapUtil.getXMax(zoom);
        mapSize.y = MapUtil.getYMax(zoom);
        pcs.firePropertyChange("zoom", oldZoom, zoom);
    }

    public void zoomIn(Point pivot) {
        if (getZoom() >= getTileServer().getMaxZoom())
            return;
        Point mapPosition = getMapPosition();
        int dx = pivot.x;
        int dy = pivot.y;
        setZoom(getZoom() + 1);
        setMapPosition(mapPosition.x * 2 + dx, mapPosition.y * 2 + dy);
        redraw();
    }

    public void zoomOut(Point pivot) {
        if (getZoom() <= 1)
            return;
        Point mapPosition = getMapPosition();
        int dx = pivot.x;
        int dy = pivot.y;
        setZoom(getZoom() - 1);
        setMapPosition((mapPosition.x - dx) / 2, (mapPosition.y - dy) / 2);
        redraw();
    }

    
    public MapWidgetContext getContext() {
		return context;
	}

	public Point getCursorPosition() {
        return new Point(
        		mapPosition.x + mouseCoords.x, 
        		mapPosition.y + mouseCoords.y);
    }

    public Point getTile(Point position) {
        return new Point(
        		(int) Math.floor(((double) position.x) / MapConstants.TILE_SIZE),
        		(int) Math.floor(((double) position.y) / MapConstants.TILE_SIZE));
    }

    public Point getCenterPosition() {
        Point size = getSize();
        return new Point(mapPosition.x + size.x / 2, mapPosition.y + size.y / 2);
    }

    public void setCenterPosition(Point p) {
        Point size = getSize();
        setMapPosition(p.x - size.x / 2, p.y - size.y / 2);
    }

    public PointD getLongitudeLatitude(Point position) {
        return new PointD(
                MapUtil.position2lon(position.x, getZoom()),
                MapUtil.position2lat(position.y, getZoom()));
    }
    
    public Point computePosition(double x1, double y1) {
        int x = MapUtil.lon2position(x1, getZoom());
        int y = MapUtil.lat2position(y1, getZoom());
        return new Point(x, y);
    }

    public Point computePosition(PointD coords) {
        int x = MapUtil.lon2position(coords.x, getZoom());
        int y = MapUtil.lat2position(coords.y, getZoom());
        return new Point(x, y);
    }
    
    //-------------------------------------------------------------------------
    // utils
    public static String format(double d) {
        return String.format("%.5f", d);
    }

    public static double getN(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return n;
    }

    public static String getTileNumber(TileServer tileServer, double lat, double lon, int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
        return tileServer.getType().getTileString(tileServer.getURL(), xtile, ytile, zoom);
    }    
}