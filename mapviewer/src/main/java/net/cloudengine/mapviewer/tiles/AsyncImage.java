package net.cloudengine.mapviewer.tiles;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import net.cloudengine.mapviewer.MapWidget;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class AsyncImage implements Runnable {

	//FIXME
	private static final int IMAGEFETCHER_THREADS = 4;
	private static ThreadPoolExecutor executor;
	
	static {
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		ThreadFactory threadFactory = new ThreadFactory( ) {
	        public Thread newThread(Runnable r) {
	            Thread t = new Thread(r);
	            t.setName("Async Image Loader " + t.getId() + " " + System.identityHashCode(t));
	            t.setDaemon(true);
	            return t;
	        }
	    };
	    executor = new ThreadPoolExecutor(IMAGEFETCHER_THREADS, 16, 2, TimeUnit.SECONDS, workQueue, threadFactory);
	}	
	
	private final AtomicReference<ImageData> imageData = new AtomicReference<ImageData>();
    private Image image; // might as well be thread-local
    private FutureTask<Boolean> task;
    private volatile long stamp;
    private final TileServer tileServer;
    private final int x, y, z;
    private MapWidget map;    
    
    
    public AsyncImage(MapWidget map, TileServer tileServer, int x, int y, int z) {
        this.tileServer = tileServer;
        this.map = map;
        this.x = x;
        this.y = y;
        this.z = z;
        task = new FutureTask<Boolean>(this, Boolean.TRUE);
        executor.execute(task);
        this.stamp = map.getZoom();
    }
    
    public void run() {
    	String url = tileServer.getType().getTileString(tileServer.getURL(), x, y, z);
        if (stamp != map.getZoom()) {
            //System.err.println("pending load killed: " + url);
            try {
                // here is a race, we just live with.
                if (!map.getDisplay().isDisposed()) {
                	map.getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            map.getCache().remove(tileServer, x, y, z);
                        }
                    });
                }
            } catch (SWTException e) {
//                log.log(Level.INFO, "swt exception during redraw display-race, ignoring");
            	//FIXME
            }
            
            return;
        }
        try {
        	//System.err.println("fetch " + url);
            //Thread.sleep(2000);
            
        	HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        	con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2");
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			con.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			con.setRequestProperty("Accept-Language", "es-ES,es;q=0.8");
			con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        	InputStream in = con.getInputStream();
            imageData.set(new ImageData(in));
            try {
                // here is a race, we just live with.
                if (!map.getDisplay().isDisposed()) {
                	map.getDisplay().asyncExec(new Runnable() {
                        public void run() {
                        	map.redraw();
                        }
                    });
                }
            } catch (SWTException e) {
            	e.printStackTrace();
//                log.log(Level.INFO, "swt exception during redraw display-race, ignoring");
            }
        } catch (Exception e) {
        	e.printStackTrace();
//            log.log(Level.SEVERE, "failed to load imagedata from url: " + url, e);
        }
    }
    
    
    public ImageData getImageData(Device device) {
        return imageData.get();
    }
    
    public Image getImage(Display display) {
        checkThread(display);
        if (image == null && imageData.get() != null) {
            image = new Image(display, imageData.get());
        }
        return image;
    }
    
    public void dispose(Display display) {
        checkThread(display);
        if (image != null) {
            //System.err.println("disposing: " + getTileString(tileServer, x, y, z));
            image.dispose();
        }
    }
    
    private void checkThread(Display display) {
        // jdk 1.6 bug from checkWidget still fails here
        if (display.getThread() != Thread.currentThread()) {
            throw new IllegalStateException("wrong thread to pick up the image");
        }
    }
	
}
