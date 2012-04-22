package net.cloudengine.mapviewer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicLong;

import net.cloudengine.mapviewer.layers.BasicLayer;
import net.cloudengine.mapviewer.layers.DebugTileLayer;
import net.cloudengine.mapviewer.layers.MapLayer;
import net.cloudengine.mapviewer.tiles.TileCache;
import net.cloudengine.mapviewer.tiles.TileServer;
import net.cloudengine.mapviewer.tiles.TileServerType;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;
import net.sf.swtgraph.layeredcanvas.ISelectableLayer;
import net.sf.swtgraph.layeredcanvas.LayeredCanvas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class MapWidget extends LayeredCanvas {
    
	private Cursor openHand;
	private Cursor closedHand;
	
	public static final class PointD {
        public double x, y;
        public PointD(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static class Stats {
        public int tileCount;
        public long dt;
        private Stats() {
            reset();
        }
        private void reset() {
            tileCount = 0;
            dt = 0;
        }
    }
    
    private class MapMouseListener implements MouseListener, MouseWheelListener, MouseMoveListener, MouseTrackListener {
        private Point mouseCoords = new Point(0, 0);
        private Point downCoords;
        private Point downPosition;
        
        public void mouseEnter(MouseEvent e) {
            MapWidget.this.forceFocus();
        }
        
        public void mouseExit(MouseEvent e) {
        }

        public void mouseHover(MouseEvent e) {
        	
        }
        
        public void mouseDoubleClick(MouseEvent e) {
            if (e.button == 1) 
                zoomIn(new Point(mouseCoords.x, mouseCoords.y));
            else if (e.button == 3)
                zoomOut(new Point(mouseCoords.x, mouseCoords.y));
        }
        public void mouseDown(MouseEvent e) {
        	MapWidget.this.setCursor(closedHand);
        	
        	if (e.button == 1 && (e.stateMask & SWT.CTRL) != 0) {
                setCenterPosition(getCursorPosition());
                redraw();
            }
            if (e.button == 1) {
                downCoords = new Point(e.x, e.y);
                downPosition = getMapPosition();
            }
            
            if (e.button == 1 && (e.stateMask & SWT.SHIFT) != 0 ) {
            	System.out.println("Click");
            	try {
                	Point p = new Point(mouseCoords.x, mouseCoords.y);
                	for(ICanvasLayer layer:  MapWidget.this.getLayers()) {
                		if (layer instanceof ISelectableLayer) {
                			
                			((ISelectableLayer)layer).selectObjects(p.x, p.y);
                		}
                	}
                	MapWidget.this.redraw();
                } catch (Exception ex) {
                	ex.printStackTrace();
                }
            }
            
        }
        public void mouseUp(MouseEvent e) {
        	MapWidget.this.setCursor(openHand);
            if (e.count == 1) {
                handleDrag(e);
            }
            downCoords = null;
            downPosition = null;
        }
        
        public void mouseMove(MouseEvent e) {
            handlePosition(e);
            handleDrag(e);
        }
        public void mouseScrolled(MouseEvent e) {
            if (e.count == 3)
                zoomIn(new Point(mouseCoords.x, mouseCoords.y));
            else if (e.count == -3)
                zoomOut(new Point(mouseCoords.x, mouseCoords.y));
        }
        
        private void handlePosition(MouseEvent e) {
            mouseCoords = new Point(e.x, e.y);
        }

        private void handleDrag(MouseEvent e) {
            if (downCoords != null) {
                int tx = downCoords.x - e.x;
                int ty = downCoords.y - e.y;
                setMapPosition(downPosition.x + tx, downPosition.y + ty);
                MapWidget.this.redraw();
            }
        }
    }
    
    /* constants ... */
    // FIXME la url base de la aplicacion la tiene que sacar por configuracion.
    public static final TileServer[] TILESERVERS = {
    	new TileServer("http://localhost:8080/webmodule/tiles", 18, TileServerType.GOOGLEMAPS),
    	new TileServer("http://localhost:8080/webmodule/tiles", 18, TileServerType.GOOGLESAT),
    	new TileServer("http://localhost:8080/webmodule/tiles", 18, TileServerType.OPENSTREET),
    	new TileServer("http://mt1.google.com/vt/lyrs=m@139&hl=es", 18, TileServerType.GOOGLEMAPS_DIRECT)    	
    };
 
    /* basically not be changed */
    private static final int TILE_SIZE = 256;
    public static final int CACHE_SIZE = 256;
    public static final int IMAGEFETCHER_THREADS = 4;
    
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Point mapSize = new Point(0, 0);
    public Point mapPosition = new Point(0, 0);
    private int zoom;
    private AtomicLong zoomStamp = new AtomicLong();

    private TileServer tileServer = TILESERVERS[0];
    private TileCache cache = new TileCache();
    private Stats stats = new Stats();
    private MapMouseListener mouseListener = new MapMouseListener();
    
    public MapWidget(Composite parent, int style) {
        this(parent, style, new Point(354083, 631905), 12);
    }
    
    public MapWidget(Composite parent, int style, Point mapPosition, int zoom) {
        super(parent, SWT.DOUBLE_BUFFERED | style);
        
        
    	ImageData openHandimageData = new ImageData(MapWidget.class.getResourceAsStream("open_hand.ico"));
    	ImageData closedHandimageData = new ImageData(MapWidget.class.getResourceAsStream("closed_hand.ico"));
    	
    	openHand = new Cursor(this.getDisplay(), openHandimageData, 0, 0);
    	closedHand = new Cursor(this.getDisplay(), closedHandimageData, 0, 0);
    	this.setCursor(openHand);
        
//         FIXME sacar del argumento del constructor
//        mapPosition.x = MapWidget.lon2position(-58.43422, zoom);
//        mapPosition.y = MapWidget.lat2position(-34.60778, zoom);
        
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
               MapWidget.this.widgetDisposed(e);
            }
        });
//        addPaintListener(new PaintListener() {
//            public void paintControl(PaintEvent e) {
//                MapWidget.this.paintControl(e);
//            }
//        });
        
        addMouseListener(mouseListener);
        addMouseMoveListener(mouseListener);
        addMouseWheelListener(mouseListener);
        addMouseTrackListener(mouseListener);
        // TODO: check tileservers
        
      this.addLayer(new MapLayer(this));
      this.addLayer(new DebugTileLayer(this));
      this.addLayer(new BasicLayer(this));
      
      setZoom(zoom);
//    setMapPosition(mapPosition);
    setCenterPosition(mapPosition);
    this.redraw();
      
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
    
    public TileCache getCache() {
        return cache;
    }
    
    public TileServer getTileServer() {
        return tileServer;
    }
    
    public void setTileServer(TileServer tileServer) {
        this.tileServer = tileServer;
        redraw();
    }
    
    public Stats getStats() {
        return stats;
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
        mapSize.x = getXMax();
        mapSize.y = getYMax();
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

    /**
     * Calcula la cantidad de tiles en el sentido x que tiene el mapa
     * @return
     */
    public int getXTileCount() {
        return (1 << zoom);
    }

    /**
     * Calcula la cantidad de tiles en el sentido y que tiene el mapa
     * @return
     */
    public int getYTileCount() {
        return (1 << zoom);
    }

    /**
     * Calcula la cantidad de pixels en el sentido x que tiene el mapa
     * @return
     */
    public int getXMax() {
        return TILE_SIZE * getXTileCount();
    }

    /**
     * Calcula la cantidad de pixels en el sentido x que tiene el mapa
     * @return
     */
    public int getYMax() {
        return TILE_SIZE * getYTileCount();
    }

    
    public Point getCursorPosition() {
        return new Point(mapPosition.x + mouseListener.mouseCoords.x, mapPosition.y + mouseListener.mouseCoords.y);
    }

    public Point getTile(Point position) {
        return new Point((int) Math.floor(((double) position.x) / TILE_SIZE),(int) Math.floor(((double) position.y) / TILE_SIZE));
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
                position2lon(position.x, getZoom()),
                position2lat(position.y, getZoom()));
    }
    
    public Point computePosition(double x1, double y1) {
        int x = lon2position(x1, getZoom());
        int y = lat2position(y1, getZoom());
        return new Point(x, y);
    }

    public Point computePosition(PointD coords) {
        int x = lon2position(coords.x, getZoom());
        int y = lat2position(coords.y, getZoom());
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

    public static double position2lon(int x, int z) {
        double xmax = TILE_SIZE * (1 << z);
        return x / xmax * 360.0 - 180;
    }

    public static double position2lat(int y, int z) {
        double ymax = TILE_SIZE * (1 << z);
        return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / ymax)));
    }

    public static double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    public static double tile2lat(int y, int z) {
        return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z))));
    }

    public static int lon2position(double lon, int z) {
        double xmax = TILE_SIZE * (1 << z);
        return (int) Math.floor((lon + 180) / 360 * xmax);
    }

    public static int lat2position(double lat, int z) {
        double ymax = TILE_SIZE * (1 << z);
        return (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * ymax);
    }

    public static String getTileNumber(TileServer tileServer, double lat, double lon, int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
        return tileServer.getType().getTileString(tileServer.getURL(), xtile, ytile, zoom);
    }
    
    public TileServer[] getServers() {
    	return TILESERVERS;
    }
}