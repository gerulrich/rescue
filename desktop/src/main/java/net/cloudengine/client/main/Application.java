package net.cloudengine.client.main;

import net.cloudengine.client.resources.ImgBundle;
import net.cloudengine.client.workbench.BluePage;
import net.cloudengine.client.workbench.CTIPage;
import net.cloudengine.client.workbench.GreenPage;
import net.cloudengine.client.workbench.Page;
import net.cloudengine.client.workbench.PageContainer;
import net.cloudengine.client.workbench.TestPage;
import net.cloudengine.rpc.controller.config.PropertyController;
import net.cloudengine.widgets.PhoneBar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Application extends ApplicationWindow {

	private PropertyController prop;
	public PageContainer pageContainer; // FIXME
	private Injector injector = Guice.createInjector(GuiceModule.BASE, GuiceModule.ASTERISK);
	
	private boolean ctiEnabled = false;
	
	public Application() {
		super(null);
		addMenuBar();
		addStatusLine();
		
	}
	
	public void setPropertyController(PropertyController prop) {
		this.prop = prop;
		ctiEnabled = prop.getProperty("asterisk.enabled").getValue(Boolean.class);
	}
	
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Rescue me!");
		this.setStatus("Recue me! - version 0.2-SNAPSHOT");
		shell.setSize(950, 710);
		
		// FIXME hacer dispose de las imagenes
		Image small = new ImgBundle().getImageDescriptor("favicon-16.png").createImage();
		Image medium = new ImgBundle().getImageDescriptor("favicon-32.png").createImage();
		Image large = new ImgBundle().getImageDescriptor("favicon-48.png").createImage();
		shell.setImages(new Image[] { small, medium, large });	
	}
	
	/**
	 * Creates the main window's contents
	 * 
	 * @param parent the main window
	 * @return Control
	 */
	protected Control createContents(Composite parent) {
		Composite parentComp = new Composite(parent, SWT.FLAT);
		
		GridLayout mlayout = new GridLayout();
		mlayout.numColumns = 1;
		mlayout.horizontalSpacing = 0;
		mlayout.verticalSpacing = 0;
		mlayout.marginHeight = 2;
		parentComp.setLayout(mlayout);
		
		CBanner ban = new CBanner(parentComp, SWT.NONE);
		ban.setLayout(new FillLayout());

		Composite toolBannerComp = new Composite(ban, SWT.NONE);
		toolBannerComp.setLayout(new FillLayout());

		Composite perspectiveBannerComp = new Composite(ban, SWT.NONE);
		perspectiveBannerComp.setLayout(new FillLayout());
		
		ban.setRightWidth(155);
		ban.setLeft(toolBannerComp);
		ban.setRight(perspectiveBannerComp);
		ban.setSimple(false);
		
		// Panel principal donde se van a ver las diferentes 
		// perspectivas de usuaria.
		Composite mainComp = new Composite(parentComp, SWT.NONE | SWT.BORDER);
		
		GridData gd0 = new GridData();
		gd0.horizontalAlignment = SWT.FILL;
		gd0.grabExcessHorizontalSpace = true;
		ban.setLayoutData(gd0);

		GridData gd1 = new GridData();
		gd1.horizontalAlignment = SWT.FILL;
		gd1.verticalAlignment = SWT.FILL;
		gd1.grabExcessHorizontalSpace = true;
		gd1.grabExcessVerticalSpace = true;
		mainComp.setLayoutData(gd1);
		
		CoolBarManager tbm = createCoolBarManager();
		tbm.createControl(toolBannerComp);
		
		ToolBarManager tbm2 = createPerspectiveToolBarManager();
		tbm2.createControl(perspectiveBannerComp);

		
		
		
		Page ctiPage = ctiEnabled ? injector.getInstance(CTIPage.class) : new BluePage();
		
		pageContainer = new PageContainer(mainComp, SWT.NONE);
		pageContainer.setPages(injector.getInstance(TestPage.class), new GreenPage(), ctiPage);
		pageContainer.showPage(0);
		
		return parentComp;
	}

	private ToolBarManager createPerspectiveToolBarManager() {
		ToolBarManager tbm2 = new ToolBarManager(SWT.RIGHT | SWT.FLAT);
	    tbm2.add(new ChangePerspectiveAction("operator.png", true, true, this, 0));
	    tbm2.add(new ChangePerspectiveAction("dispatcher.png", true, false, this, 1));
	    tbm2.add(new ChangePerspectiveAction("phone.png", ctiEnabled, false, this, 2));
	    tbm2.add(new ChangePerspectiveAction("supervisor.png", false, false,  this,3));
	    tbm2.add(new ChangePerspectiveAction("magnifier2.png", true, false,  this,0));
		return tbm2;
	}

	private CoolBarManager createCoolBarManager() {
		CoolBarManager tbm = new CoolBarManager(SWT.RIGHT  | SWT.FLAT);
	    IToolBarManager tb1 = new ToolBarManager(SWT.NONE);
	    tb1.add(new Action() {
	    	{
	    		setImageDescriptor(ImgBundle.getImgBundle().getImageDescriptor("ticket.png"));
	    	}});
		tbm.add(tb1);
		if (ctiEnabled) {
			tbm.add(injector.getInstance(PhoneBar.class));
		}
		
		
		return tbm;
	}

	/**
	 * Creates the menu for the application
	 * 
	 * @return MenuManager
	 */
	protected MenuManager createMenuManager() {
		MenuManager menu = new MenuManager();
		MenuManager appMenu = new MenuManager("&App");
		appMenu.add(new Action("&Salir") {
			@Override
			public void run() {
				System.exit(0);
			}});
		menu.add(appMenu);
		
		MenuManager toolsMenu = new MenuManager("&Herramientas");
		toolsMenu.add(new Action() {
			{
				setText("Ver configuración...");
			}

			@Override
			public void run() {
				PropertiesWindow pw = new PropertiesWindow(getShell(), prop);
				pw.open();
			}
			
			
		});
		menu.add(toolsMenu);
		
		return menu;
	}

	/**
	 * Creates the coolbar for the application
	 */
	protected CoolBarManager createCoolBarManager(int style) {
		CoolBarManager cbm = new CoolBarManager(style);
		IToolBarManager tb2 = new ToolBarManager(style);
		tb2.add(new Action() {{setText("&Button4");}});
		tb2.add(new Action() {{setText("&Button5");}});
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