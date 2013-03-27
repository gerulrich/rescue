package net.cloudengine.app;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import net.cloudengine.app.actions.AboutAction;
import net.cloudengine.app.actions.ExitAction;
import net.cloudengine.app.actions.layers.ChangeBaseLayerAction;
import net.cloudengine.app.actions.layers.ChangeOverlayLayerAction;
import net.cloudengine.app.actions.zoom.ZoomGeneralAction;
import net.cloudengine.app.actions.zoom.ZoomInAction;
import net.cloudengine.app.actions.zoom.ZoomOutAction;
import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.pages.IndexPage;
import net.cloudengine.mapviewer.pages.InfoPage;
import net.cloudengine.mapviewer.pages.PageContainer;
import net.cloudengine.mapviewer.pages.SearchPage;
import net.cloudengine.mapviewer.pages.selection.SeleccionPage;
import net.cloudengine.mapviewer.tiles.TileServer;
import net.cloudengine.mapviewer.tools.AbstractTool;
import net.cloudengine.mapviewer.tools.ChangeToolAction;
import net.cloudengine.mapviewer.tools.ZoomTool;
import net.cloudengine.mapviewer.tools.selection.SelectionTool;
import net.sf.swtgraph.layeredcanvas.ICanvasLayer;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class keeps track of you library, and who you've loaned books to
 */
public abstract class MapApplication extends ApplicationWindow {

	// The actions
	private ExitAction exitAction;
	private ZoomInAction zoomInAction;
	private ZoomOutAction zoomOutAction;
	private ZoomGeneralAction zoomGeneralAction;
	private AboutAction aboutAction;

	private MapWidget mapWidget;
	private MapWidgetContext context;
	
	/**
	 * Librarian constructor
	 */
	public MapApplication(Map<String,Object> props) {
		super(null);
		// Create the actions
		exitAction = new ExitAction(this);
		zoomInAction = new ZoomInAction(this);
		zoomOutAction = new ZoomOutAction(this);
		zoomGeneralAction = new ZoomGeneralAction(this);
		aboutAction = new AboutAction(this);

		init(props);
		addMenuBar();
		addCoolBar(SWT.NONE);
		addStatusLine();
		
	}
	
	public abstract void init(Map<String,Object> props);
	

	/**
	 * Configures the shell
	 * 
	 * @param shell the shell
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		// Set the title bar text
		shell.setText("Rescue me! (Mapa)");
		this.setStatus("Recue me! - version 0.2-SNAPSHOT");

		Display display = Display.getCurrent();

		// FIXME hacer dispose de las imagenes
		Image small = new Image(display, MapApplication.class.getResourceAsStream("starthere_16.png"));
		Image medium = new Image(display,MapApplication.class.getResourceAsStream("starthere_32.png"));
		Image large = new Image(display, MapApplication.class.getResourceAsStream("starthere_48.png"));

		shell.setSize(950, 710);
		shell.setImages(new Image[] { small, medium, large });

	}

	public MapWidget getMapWidget() {
		return mapWidget;
	}

	/**
	 * Creates the main window's contents
	 * 
	 * @param parent the main window
	 * @return Control
	 */
	protected Control createContents(Composite parent) {

		// Create the SashForm
		Composite sash = new Composite(parent, SWT.NONE);
		sash.setLayout(new FillLayout());
		sash.setLayoutData(new GridData(GridData.FILL_BOTH));
		final SashForm sashForm = new SashForm(sash, SWT.HORIZONTAL);

		// Change the width of the sashes
		sashForm.SASH_WIDTH = 5;
		// Change the color used to paint the sashes
//		sashForm.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));

		// Create the buttons and their event handlers
		PageContainer pageContainer = new PageContainer(sashForm, SWT.None);

		mapWidget = new MapWidget(sashForm, SWT.BORDER, context);
		
		final InfoPage infoPage =new InfoPage(this);
		
		pageContainer.setPages(
				new IndexPage(), 
				new SearchPage(this),
				infoPage,
				new SeleccionPage(this));
		pageContainer.showPage(0);
		
		mapWidget.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                infoPage.updateInfos();
            }
        });
		
		sashForm.setWeights(new int[] { 1, 4 });

		return sash;
	}
	
	/**
	 * Creates the menu for the application
	 * 
	 * @return MenuManager
	 */
	
	protected MenuManager createMenuManager() {
		context = new MapWidgetContext();
		configureMap(context);
		
		// Crear el menu principal.
		MenuManager mm = new MenuManager();
		// Crear el menu archivo.
		MenuManager fileMenu = new MenuManager("&Archivo");
		mm.add(fileMenu);

		// Agregar las acciones al menu Archivo.
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);

		// Crear el menu herramientas
		MenuManager toolsMenu = new MenuManager("&Herramientas");
		mm.add(toolsMenu);

		// Agregar al acciones al menu Herramientas.
		toolsMenu.add(zoomInAction);
		toolsMenu.add(zoomOutAction);
		toolsMenu.add(zoomGeneralAction);

		// Crear el menu Capas.
		MenuManager layerMenu = new MenuManager("&Capas");
		mm.add(layerMenu);

						
		// Agregar las acciones al menu Capas.
		layerMenu.add(new Separator("provider"));
		List<TileServer> tileServers = context.getBaseTileServers();
		for(int i = 0; i < tileServers.size(); i++) {
			TileServer ts = tileServers.get(i);
			layerMenu.add(new ChangeBaseLayerAction(ts.getName(), this, i, i == 0));
		}
		
		layerMenu.add(new Separator("overlayrs"));
		for(ICanvasLayer layer : context.getlayers()) {
			if (layer.showLegend()) {
				layerMenu.add(new ChangeOverlayLayerAction(layer.getName(), this, layer));
			}
		}
		
		// Crear el menu ayuda.
		MenuManager helpMenu = new MenuManager("Ayuda");
		mm.add(helpMenu);

		// Agregar acciones al menu ayuda.
		helpMenu.add(aboutAction);
		
		return mm;
	}

	/**
	 * Creates the coolbar for the application
	 */
	protected CoolBarManager createCoolBarManager(int style) {

		CoolBarManager cbm = new CoolBarManager(style);

		IToolBarManager zoomTmb = new ToolBarManager(style);
		IToolBarManager toolTmb = new ToolBarManager(style);

		zoomTmb.add(new ZoomInAction(this));
		zoomTmb.add(new ZoomOutAction(this));
		zoomTmb.add(new ZoomGeneralAction(this));
		
		zoomTmb.add(new Separator());
//		zoomTmb.add(new SelectAction(null));
		
		for(AbstractTool tool : this.context.getTools()) {
			toolTmb.add(new ChangeToolAction("adasd", tool, tool.isEnabled()) {
			});
		}

		cbm.add(zoomTmb);
		cbm.add(toolTmb);

		return cbm;
	}

	/**
	 * Creates the status line manager
	 */
	protected StatusLineManager createStatusLineManager() {
		return new StatusLineManager();
	}

	/**
	 * Shows an error
	 * 
	 * @param msg the error
	 */
	public void showError(String msg) {
		MessageDialog.openError(getShell(), "Error", msg);
	}

	/**
	 * Closes the application
	 */
	public boolean close() {
		return super.close();
	}
	
	protected void configureMap(MapWidgetContext context) {
		context.addTool(new ZoomTool(context));
		context.addTool(new SelectionTool(context));
	}

}
