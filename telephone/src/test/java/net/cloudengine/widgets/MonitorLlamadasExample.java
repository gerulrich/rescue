package net.cloudengine.widgets;

import net.cloudengine.new_.cti.asterisk.AsteriskTAPIDriver;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MonitorLlamadasExample extends ApplicationWindow {

	public MonitorLlamadasExample() {
		super(null);
		setBlockOnOpen(true);
		open();
		Display.getCurrent().dispose();
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Monitor de llamadas");
		shell.setSize(350, 400);
	}

	protected Control createContents(Composite parent) {
		AsteriskTAPIDriver driver = new AsteriskTAPIDriver("192.168.0.104", "manager", "manager");
		CallWatcher widget = new CallWatcher(driver.createEventProvider());
		Control control = widget.createControl(parent);
		return control;
	}

	public static void main(String[] args) throws Exception {
		new MonitorLlamadasExample();
	}
}