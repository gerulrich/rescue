package net.cloudengine.widgets.panel;

import net.cloudengine.widgets.sound.Settings;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PhonePanelExample extends ApplicationWindow {

	private PhonePanel p;
	
	public PhonePanelExample() {
		super(null);
		setBlockOnOpen(true);
		open();
		Display.getCurrent().dispose();
		p.dispose();
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Soft phone");
		shell.setSize(250, 420);
	}
	
	protected Control createContents(Composite parent) {
		Settings s = new Settings("4001", "sip4001", "192.168.0.104", 5060); 
		p = new PhonePanel(s, new EventInterceptorTest(),parent, SWT.NONE);
		return p;
	}

	public static void main(String[] args) {
		new PhonePanelExample();
		System.exit(0);
	}
	
	class EventInterceptorTest implements EventInterceptor{

		@Override
		public void onCallStart() {
			System.out.println("Comienza una llamada");
		}

		@Override
		public void onCallEnd() {
			System.out.println("T una llamada");
		}
		
	};

}
