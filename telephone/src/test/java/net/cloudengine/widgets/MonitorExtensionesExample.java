package net.cloudengine.widgets;

import net.cloudengine.new_.cti.TAPIDriver;
import net.cloudengine.new_.cti.asterisk.AsteriskTAPIDriver;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MonitorExtensionesExample extends ApplicationWindow {

	public MonitorExtensionesExample() {
		super(null);
		setBlockOnOpen(true);
		open();
		Display.getCurrent().dispose();
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Monitor de extensiones");
		shell.setSize(200, 400);
	}

	protected Control createContents(Composite parent) {
		TAPIDriver driver = new AsteriskTAPIDriver("192.168.0.104", "manager", "manager");
		PhonesMonitorWidget widget = new PhonesMonitorWidget(driver.createEventProvider());
		Control control = widget.createControl(parent);
		driver.init();
		return control;
	}

	public static void main(String[] args) {
		new MonitorExtensionesExample();
		System.exit(0);
	}
}