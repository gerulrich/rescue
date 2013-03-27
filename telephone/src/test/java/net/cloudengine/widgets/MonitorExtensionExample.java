package net.cloudengine.widgets;

import net.cloudengine.new_.cti.TAPIDriver;
import net.cloudengine.new_.cti.asterisk.AsteriskTAPIDriver;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

//import com.google.inject.Guice;
//import com.google.inject.Injector;

public class MonitorExtensionExample extends ApplicationWindow {

	public MonitorExtensionExample() {
		super(null);
	    addCoolBar(SWT.NONE);
	}
	
	@Override
	protected CoolBarManager createCoolBarManager(int style) {
		
//		Injector injector = Guice.createInjector(new AsteriskModule());
//		PhoneBar phoneBar = injector.getInstance(PhoneBar.class);
		
		TAPIDriver driver = new AsteriskTAPIDriver("192.168.0.104", "manager", "manager");
		PhoneBar phoneBar = new PhoneBar(driver.createEventProvider());
		phoneBar.setPhoneNumber("2002");
		
		CoolBarManager cbm = new CoolBarManager();
		ToolBarManager tbm = new ToolBarManager();
		tbm.add(new Action() {
			{
				setText("Hola");
			}
			
		});
		cbm.add(tbm);
		cbm.add(phoneBar);
		driver.init();
		return cbm;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Monitor de ext. telefónicas");
		shell.setSize(400, 400);
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		return composite;
	}

	public static void main(String[] args) {
		MonitorExtensionExample app = new MonitorExtensionExample();
		app.setBlockOnOpen(true);
		app.open();
		Display.getCurrent().dispose();
		System.exit(0);
	}

}
