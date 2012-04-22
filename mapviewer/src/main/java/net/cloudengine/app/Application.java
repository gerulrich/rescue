package net.cloudengine.app;

import net.cloudengine.app.actions.AboutAction;
import net.cloudengine.app.actions.ExitAction;
import net.cloudengine.app.actions.SelectAction;
import net.cloudengine.app.actions.layers.GoogleMapsSatelitalAction;
import net.cloudengine.app.actions.layers.GoogleMapsStreetAction;
import net.cloudengine.app.actions.layers.GoogleMapsStreetDirectAction;
import net.cloudengine.app.actions.layers.OpenStreetMapAction;
import net.cloudengine.app.actions.zoom.ZoomGeneralAction;
import net.cloudengine.app.actions.zoom.ZoomInAction;
import net.cloudengine.app.actions.zoom.ZoomOutAction;
import net.cloudengine.mapviewer.MapWidget;
import net.cloudengine.mapviewer.PageContainer;
import net.cloudengine.mapviewer.SearchPage;

import org.eclipse.jface.action.Action;
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
public class Application extends ApplicationWindow {

	// The actions
	private ExitAction exitAction;
	private ZoomInAction zoomInAction;
	private ZoomOutAction zoomOutAction;
	private ZoomGeneralAction zoomGeneralAction;
	private AboutAction aboutAction;

	private MapWidget mapWidget;

	/**
	 * Librarian constructor
	 */
	public Application() {
		super(null);
		// Create the actions
		exitAction = new ExitAction(this);
		zoomInAction = new ZoomInAction(this);
		zoomOutAction = new ZoomOutAction(this);
		zoomGeneralAction = new ZoomGeneralAction(this);
		aboutAction = new AboutAction(this);

		addMenuBar();
		addCoolBar(SWT.NONE);
		addStatusLine();
	}

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
		Image small = new Image(display, Application.class.getResourceAsStream("starthere_16.png"));
		Image medium = new Image(display,Application.class.getResourceAsStream("starthere_32.png"));
		Image large = new Image(display, Application.class.getResourceAsStream("starthere_48.png"));

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
		pageContainer.setPages(new SearchPage(this));
		pageContainer.showPage(0);

		mapWidget = new MapWidget(sashForm, SWT.BORDER);

		sashForm.setWeights(new int[] { 1, 4 });

		return sash;
	}

	/**
	 * Creates the menu for the application
	 * 
	 * @return MenuManager
	 */
	protected MenuManager createMenuManager() {
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
		layerMenu.add(new GoogleMapsStreetAction(this));
		layerMenu.add(new GoogleMapsSatelitalAction(this));
		layerMenu.add(new OpenStreetMapAction(this));
		layerMenu.add(new GoogleMapsStreetDirectAction(this));

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
		IToolBarManager tb2 = new ToolBarManager(style);

		zoomTmb.add(new ZoomInAction(this));
		zoomTmb.add(new ZoomOutAction(this));
		zoomTmb.add(new ZoomGeneralAction(this));
		zoomTmb.add(new Separator());
		zoomTmb.add(new SelectAction(null));

		tb2.add(new Action() {
			{
				setText("&Button4");
			}
		});

		tb2.add(new Action() {
			{
				setText("&Button5");
			}
		});

		cbm.add(zoomTmb);
		cbm.add(tb2);

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

}
