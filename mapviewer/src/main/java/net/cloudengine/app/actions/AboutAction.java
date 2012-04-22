package net.cloudengine.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;

/**
 * This action class shows an About box
 */

public class AboutAction extends Action {

	private ApplicationWindow window;

	public AboutAction(ApplicationWindow window) {
		super("&Acerca de@Ctrl+A");
		setToolTipText("About");
		this.window = window;
	}

	/**
	 * Shows an about box
	 */
	public void run() {
		MessageDialog.openInformation(window.getShell(), "Acerca de...", "Mapa");
	}
}