package net.cloudengine.widgets;

import net.cloudengine.pbx.asterisk.AsteriskModule;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.inject.Guice;
import com.google.inject.Injector;

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
		Injector injector = Guice.createInjector(new AsteriskModule());
		CallWatcher widget = injector.getInstance(CallWatcher.class);
		Control control = widget.createControl(parent);
		return control;
	}

	public static void main(String[] args) throws Exception {
		new MonitorLlamadasExample();
	}
}