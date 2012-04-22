package net.cloudengine.client.main;

import net.cloudengine.client.resources.ImgBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class ChangePerspectiveAction extends Action {
	
	private Application app;
	private int index;

	public ChangePerspectiveAction(String imagefile, boolean enabled, boolean checked, Application app, int index) {
		super(null, SWT.PUSH);
		setChecked(checked);
		setImageDescriptor(ImgBundle.getImgBundle().getImageDescriptor(imagefile));
		setEnabled(enabled);
		this.app = app;
		this.index = index;
	}
	
	

	@Override
	public void run() {
		if (app.pageContainer != null && index >= 0) {
			app.pageContainer.showPage(index);
			app.pageContainer.layout();
		}
	}
	
	
}
