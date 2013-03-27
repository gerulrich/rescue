
package net.cloudengine.mapviewer.pages;
import net.cloudengine.app.MapApplication;
import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.util.MapConstants;
import net.cloudengine.mapviewer.util.MapUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class InfoPage extends AbstractPage implements Page {
    
//    private static final Logger log = Logger.getLogger(InfoPage.class.getName());
    
//    private final MapBrowser mapBrowser;
    private Table table;
    private Spec[] specs;
    private MapApplication app;
    
    public InfoPage(MapApplication app) {
//        this.mapBrowser = mapBrowser;
    	this.app = app;
        specs = new Spec[] {
                new Spec("Zoom") { public String computeValue() { return Integer.toString(mapWidget.getZoom()); }},
                new Spec("Tamaño del mapa") { public String computeValue() { Point size = mapWidget.getSize(); return size.x + ", " + size.y; }},
                new Spec("Posición del mapa") { public String computeValue() { Point position = mapWidget.getMapPosition(); return position.x + ", " + position.y; }},
                new Spec("Posición central") { public String computeValue() { Point position = mapWidget.getCenterPosition(); return position.x + ", " + position.y; }},
                new Spec("Tiempo de renderizado") { public String computeValue() { mapWidget.getStats(); return mapWidget.getStats().dt + " ms"; }},
                new Spec("Subprocesos de obtención de imagenes") { public String computeValue() { return Integer.toString(MapConstants.IMAGEFETCHER_THREADS); }},
                new Spec("Cantidad de tiles dibujados") {
                    public String computeValue() {
                        mapWidget.getStats(); 
                        return mapWidget.getStats().tileCount+"";
                    }
                },
                new Spec("Tilecache") { public String computeValue() { return String.format("%3d / %3d", mapWidget.getCache().getSize(), MapConstants.CACHE_SIZE); }},
                new Spec("Longitude/Latitude") {
                    public String computeValue() {
                        Point p = mapWidget.getCursorPosition();
                        int zoom = mapWidget.getZoom();
                        return MapWidget.format(MapUtil.position2lon(p.x, zoom)) + ", " + MapWidget.format(MapUtil.position2lat(p.y, zoom));
                    }
                },
                
        };
    }
    
    public void updateInfos() {
        if (table == null)
            return;
        for (int i = 0; i < specs.length; ++i) {
            Spec spec = specs[i];
            TableItem item = table.getItem(i);
            item.setText(1, spec.computeValue());
        }
    }

    
    protected void initContent(final PageContainer container, Composite composite) {
        addHeaderRow(container, composite, "Acciones");
        addActionLink(container, composite, "<a>Volver al menú principal</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(0);
            }
        });
        
        addHeaderRow(container, composite, "Información");
//        addInfoText(container, composite, 
//        		"The following search results were retrieved from openstreetmap.org. " +
//                "Double-click to open a location.");
        
        table = new Table(composite, SWT.FULL_SELECTION  | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2 , 1));
        
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn column1 = new TableColumn(table, SWT.NONE);
        column1.setText("Nombre");
        column1.setWidth(120);
        TableColumn column2 = new TableColumn(table, SWT.NONE);
        column2.setText("Valor");
        column2.setWidth(160);
        
        for (Spec spec : specs) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, spec.key);
        }
        
        addHeaderRow(container, composite, "Comentarios de los autores");
        addInfoText(container, composite,
        		"El número de subprocesos de obtención de imagenes determina el número de tareas en segundo " +
        		"plano se ejecutan simultáneamente en busca de " +
        		"fragmentos (tiles) que conforman el mapa. ");
        
        updateInfos();
    }

    protected void widgetDisposed(DisposeEvent e) {
    }
    
    private abstract class Spec {
        final String key;
        final MapWidget mapWidget;
        abstract String computeValue();
        
        Spec(String key) { 
        	this.key = key; 
        	this.mapWidget = app.getMapWidget(); 
        }
    }
    

}
