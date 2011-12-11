/*
 * $HeadURL$
 *
 * (c)2010 Stepan Rutz, Licensed under LGPL License
 */

package net.cloudengine.mapviewer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MapBrowser extends Composite {
    private SashForm sashForm;
    private PageContainer pageContainer;
    private MapWidget mapWidget;
    private SearchPage searchPage;
    private ResultsPage resultsPage;
    private InfoPage infoPage;

    public MapBrowser(Composite parent, int style) {
        super(parent, style);
        
        setLayout(new FillLayout());
        
        sashForm = new SashForm(this, SWT.HORIZONTAL);
        sashForm.setLayout(new FillLayout());

        pageContainer = new PageContainer(sashForm, SWT.NONE);
        mapWidget = new MapWidget(sashForm, SWT.NONE);
        
        sashForm.setWeights(new int[] { 100, 200 });
         
        searchPage = new SearchPage(this);
        resultsPage = new ResultsPage(this);
        infoPage = new InfoPage(this);
        pageContainer.setPages(searchPage, resultsPage, infoPage);
        pageContainer.showPage(0);
        
        mapWidget.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                infoPage.updateInfos();
            }
        });
        mapWidget.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                infoPage.updateInfos();
            }
        });
        mapWidget.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
                infoPage.updateInfos();
            }
        });
    }
    
    public MapWidget getMapWidget() {
        return mapWidget;
    }
    
    public SearchPage getSearchPage() {
        return searchPage;
    }
    
    public InfoPage getInfoPage() {
        return infoPage;
    }
    
    public ResultsPage getResultsPage() {
        return resultsPage;
    }
    
    public PageContainer getPageContainer() {
        return pageContainer;
    }
    
    private void createMenu(Shell shell) {
        Menu bar = new Menu (shell, SWT.BAR);
        shell.setMenuBar (bar);
        
        MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
        fileItem.setText ("&Archivo");
        Menu submenu = new Menu (shell, SWT.DROP_DOWN);
        fileItem.setMenu (submenu);
        
        MenuItem item = new MenuItem (submenu, SWT.PUSH);
        item.addListener (SWT.Selection, new Listener () {
            public void handleEvent (Event e) {
                Runtime.getRuntime().halt(0);
            }
        });
        item.setText ("&Salir\tCtrl+S");
        item.setAccelerator(SWT.MOD1 + 'S');
        
        shell.addListener(SWT.Close, new Listener() {
        	public void handleEvent(Event event) {
        		Runtime.getRuntime().halt(0);
        	}
        });
    }
    
    public static void main (String [] args) throws Exception {
    	System.out.println(MapBrowser.getWebAppContextUrl());
    	
    	Display display = new Display ();
        
        Image small = new Image(display, MapBrowser.class.getResourceAsStream("starthere_16.png"));
        Image medium = new Image(display, MapBrowser.class.getResourceAsStream("starthere_32.png"));
        Image large = new Image(display, MapBrowser.class.getResourceAsStream("starthere_48.png"));
        
        Shell shell = new Shell(display);
        shell.setText("Map Widget - SWT Native Map Browsing, Map data from openstreetmap.org");
        shell.setSize(950, 710);
        
        shell.setImages(new Image[] {small, medium, large});
        shell.setLocation(10, 10);
        
        
        shell.setMaximized(true);
        shell.setLayout (new FillLayout());

        MapBrowser mb = new MapBrowser(shell, SWT.NONE);
        mb.createMenu(shell);
        
        shell.open ();
        
        
        
        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }
    
    /**
     * Uses the JNLP API to determine the webapp context.
     * If used outside of webstart, <code>fallBackWebAppContextUrl</code> is returned.
     * For example this could return <code>http://localhost:8080/mywebapp/</code>.
     *
     * @return the url to the webapp ending with a slash
     */
    public static String getWebAppContextUrl() {
    	String webAppContextUrl;
    	try {
    		BasicService basicService = (BasicService) ServiceManager.lookup( "javax.jnlp.BasicService" );
    		String codeBase = basicService.getCodeBase().toExternalForm();
    		if ( !codeBase.endsWith( "/" )) {
    			codeBase += "/";
    		}
    		return codeBase;

    	} catch ( UnavailableServiceException e ) {
    		// TODO logging
    		webAppContextUrl = "fallBackWebAppContextUrl";
      }
      return webAppContextUrl;
    }

}


