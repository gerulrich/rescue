package net.cloudengine.app;

import org.eclipse.swt.widgets.Display;

public class MainMap {

	public static void main(String[] args) {
		Application app = new Application();
		app.setBlockOnOpen(true);
	    // Open the main window
	    app.open();
	    // Dispose the display
	    Display.getCurrent().dispose();
	}

}
