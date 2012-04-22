package net.cloudengine.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog {

	private MessageBox box;

	public AboutDialog(Shell shell) {
		box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
		box.setText("About...");
	}

	public void setMessage(String message) {
		box.setMessage(message);
	}

	public int open() {
		return box.open();
	}
}