
package net.cloudengine.mapviewer.pages;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParserFactory;

import net.cloudengine.app.MapApplication;
import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.util.PointD;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SearchPage extends AbstractPage implements Page {
    
    private static final Logger log = Logger.getLogger(SearchPage.class.getName());
    
    public static final class SearchResult {
        private String type;
        private double lat, lon;
        private String name;
        private String category;
        private String info;
        private int zoom;
        private String description = "";

        public SearchResult() {
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public double getLat() {
            return lat;
        }
        public void setLat(double lat) {
            this.lat = lat;
        }
        public double getLon() {
            return lon;
        }
        public void setLon(double lon) {
            this.lon = lon;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }
        public String getInfo() {
            return info;
        }
        public void setInfo(String info) {
            this.info = info;
        }
        public int getZoom() {
            return zoom;
        }
        public void setZoom(int zoom) {
            this.zoom = zoom;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String toString() {
            return "SearchResult [category=" + category + ", info=" + info + ", lat=" + lat + ", lon=" + lon
                    + ", name=" + name + ", type=" + type + ", zoom=" + zoom + ", description=" + description + "]";
        }

    }
    private MapApplication app;
    private ProgressBar progressBar;
    private GridData progressBarLayoutData;
    private Text searchText;
    private boolean searching;
    
//    private String oldSearch = "";
    private ArrayList<SearchResult> results = new ArrayList<SearchResult>();
    private Link searchLink;
    
    public SearchPage(MapApplication app) {
        this.app = app;
    }
    
    protected void initContent(final PageContainer container, Composite composite) {
    	addHeaderRow(container, composite, "Acciones");
    	addActionLink(container, composite, "<a>Volver al menu</a>", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				container.showPage(0);
			}
    		
    	});
    	
    	addHeaderRow(container, composite, "Infopanel");
        addInfoText(container, composite, "Toggling the tile-server changes the way " +
        		"the map is rendered. Click on the mapinfos link to see details " +
        		"about the currently rendered part of the map.");
        
        
//        addActionLink(container, composite, "<a>Cambiar fuente de datos</a>", new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent e) {
//            	MapWidget mapWidget = app.getMapWidget();
//                int next = (Arrays.asList(MapWidget.TILESERVERS).indexOf(mapWidget.getTileServer()) + 1) % MapWidget.TILESERVERS.length;
//                mapWidget.setTileServer(MapWidget.TILESERVERS[next]);
//            }
//        });

//        addActionLink(container, composite, "Ver <a>Technical Mapinfos</a>", new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent e) {
//                mapBrowser.getPageContainer().showPage(mapBrowser.getPageContainer().indexOfPage(mapBrowser.getInfoPage()));
//            }
//        });
        
        addActionLink(container, composite, "Ver <a>FIUBA</a>", new SelectionAdapter() {

        	public void widgetSelected(SelectionEvent e) {
                MapWidget mapWidget = app.getMapWidget();
                mapWidget.setZoom(16);
                Point position = mapWidget.computePosition(new PointD(-58.36798, -34.61761)); 
                mapWidget.setCenterPosition(position);
                mapWidget.redraw();
            }
        });
        
        addActionLink(container, composite, "Ver <a>FullMap</a>", new SelectionAdapter() {

        	public void widgetSelected(SelectionEvent e) {
                MapWidget mapWidget = app.getMapWidget();
                mapWidget.setZoom(12);
                Point position = new Point(354083, 631905); 
                mapWidget.setCenterPosition(position);
                mapWidget.redraw();
            }
        });
        
        
        addHeaderRow(container, composite, "Search location");
        addInfoText(container, composite, "Enter any location or landmark site to search openstreetmap's " +
                "genuine namefinder database. Hit return to perform the search.");
        
        {
            Label label = new Label(composite, SWT.NONE);
            label.setText("Search:");
            label.setLayoutData(new GridData());
            container.adapt(label);
        }
        {
            Composite wrap = new Composite(composite, SWT.NONE);
            wrap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            container.adapt(wrap);

            GridLayout layout = new GridLayout(2, false);
            //layout.marginLeft = 8;
            //layout.marginHeight = 0;
            wrap.setLayout(layout);
            
            searchText = new Text(wrap, SWT.BORDER | SWT.SEARCH);
            searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            searchText.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    startSearch();
                }
                public void widgetDefaultSelected(SelectionEvent e) {
                    startSearch();
                }
            });
            searchLink = new Link(wrap, SWT.NONE);
            searchLink.setText("<a>Go</a>");
            container.adapt(searchLink);
            searchLink.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    startSearch();
                }
            });
        }
        
        progressBar = new ProgressBar(composite, SWT.SMOOTH | SWT.INDETERMINATE);
        container.adapt(progressBar);
        progressBarLayoutData = new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2 , 1);
        progressBarLayoutData.exclude = true;
        progressBar.setVisible(false);
        progressBar.setLayoutData(progressBarLayoutData);
        
//        {
//            Label label = new Label(composite, SWT.NONE);
//            label.setText("Recent searches:");
//            label.setLayoutData(new GridData());
//            container.adapt(label);
//        }
//        
//        Combo combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
//        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
       
            
//        table = new Table(composite, SWT.FULL_SELECTION  | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
//        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        
        addHeaderRow(container, composite, "Actions");
        // FIXME
        addInfoText(container, composite, /*MapWidget.ABOUT_MSG*/"About message");
    }

    protected void widgetDisposed(DisposeEvent e) {
    }
    
    public void startSearch() {
        if (searching)
            return;
        final String search = searchText.getText();
        if (search == null || search.length() == 0)
            return;
//        else if (oldSearch != null && oldSearch.equals(search))
//            return;
        searching = true;
        setupSearchGui();
        Thread t = new Thread(new Runnable() {
            public void run() {
                doSearchInternal(search);
            }
        });
        t.setName("Background Seacher \"" + search + "\"");
        t.start();
    }
    
    private void setupSearchGui() {
        progressBarLayoutData.exclude = false;
        progressBar.setVisible(true);
        searchText.setEnabled(false);
        searchLink.setEnabled(false);
        progressBar.getParent().layout();
    }
    
    private void tearDownSearchGui() {
        progressBarLayoutData.exclude = true;
        progressBar.setVisible(false);
        searchText.setEnabled(true);
        searchLink.setEnabled(true);
        progressBar.getParent().layout();
    }
    
    private void doSearchInternal(final String newSearch) {
        results.clear();
        try {
            String args = URLEncoder.encode(newSearch, "UTF-8");
            // FIXME
            String path = "http://"/*MapWidget.NAMEFINDER_URL */+ "?find= " + args;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.newSAXParser().parse(path, new DefaultHandler() {
                private final ArrayList<String> pathStack = new ArrayList<String>();
                private final ArrayList<SearchResult> namedStack = new ArrayList<SearchResult>();
                private StringBuilder chars;
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    pathStack.add(qName);
//                    String path = getPath();
                    if ("named".equals(qName)) {
                        SearchResult result = new SearchResult();
                        result.setType(attributes.getValue("type"));
                        result.setLat(tryDouble(attributes.getValue("lat")));
                        result.setLon(tryDouble(attributes.getValue("lon")));
                        result.setName(attributes.getValue("name"));
                        result.setCategory(attributes.getValue("category"));
                        result.setInfo(attributes.getValue("info"));
                        result.setZoom(tryInteger(attributes.getValue("zoom")));
                        namedStack.add(result);
                        if (pathStack.size() == 2)
                            results.add(result);
                    } else if ("description".equals(qName)) {
                        chars = new StringBuilder();
                    }
                }
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if ("named".equals(qName)) {
                        namedStack.remove(namedStack.size() - 1);
                    } else if ("description".equals(qName)) {
                        namedStack.get(namedStack.size() - 1).setDescription(chars.toString());
                    }
                    pathStack.remove(pathStack.size() - 1);
                }
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if(chars != null)
                        chars.append(ch, start, length);
                }
//                private String getPath() {
//                    StringBuilder sb = new StringBuilder();
//                    for (String p : pathStack)
//                        sb.append("/").append(p);
//                    return sb.toString();
//                }
                private double tryDouble(String s) {
                    try {
                        return Double.valueOf(s);
                    } catch (Exception e) {
                        return 0d;
                    }
                }
                private int tryInteger(String s) {
                    try {
                        return Integer.valueOf(s);
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });
        } catch (Exception e) {
            log.log(Level.SEVERE, "failed to search for \"" + newSearch + "\"", e);
        }
//        oldSearch = newSearch;
        
        getComposite().getDisplay().asyncExec(new Runnable() {
            public void run() {
                try {
//                    ResultsPage resultsPage = mapBrowser.getResultsPage();
//                    resultsPage.setSearch(newSearch);
//                    mapBrowser.getPageContainer().showPage(mapBrowser.getPageContainer().indexOfPage(resultsPage));
//                    resultsPage.setResults(results.toArray(new SearchResult[results.size()]));
                    //System.err.println(html_);
                } finally {
                    searching = false;
                    tearDownSearchGui();
                }
            }
        });
    }
}
