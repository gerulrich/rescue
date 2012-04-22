package net.cloudengine.cti;

import net.cloudengine.cti.utils.AsteriskTestModule;
import net.cloudengine.cti.utils.EventGenerator;
import net.cloudengine.widgets.CallWatcher;

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
		String fileName = TwoPartyCallTest.class.getResource("test_multiples_llamadas.txt").getFile();
		EventGenerator generator = new EventGenerator(fileName, true);
		Injector injector = Guice.createInjector(new AsteriskTestModule(generator));
		CallWatcher widget = injector.getInstance(CallWatcher.class);
		Control control = widget.createControl(parent);
		
		generator.start();
		return control;
	}

	public static void main(String[] args) throws Exception {
		new MonitorLlamadasExample();
	}
}