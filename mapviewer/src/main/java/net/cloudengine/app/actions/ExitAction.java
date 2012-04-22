package net.cloudengine.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

/**
 * This action class exits the application
 */

public class ExitAction extends Action {

	private ApplicationWindow window;

	/**
	 * ExitAction constructor
	 */
	public ExitAction(ApplicationWindow window) {
		super("S&alir@Alt+F4");
		setToolTipText("Salir");
		this.window = window;
	}

	/**
	 * Exits the application
	 */
	public void run() {
		window.close();
	}
}