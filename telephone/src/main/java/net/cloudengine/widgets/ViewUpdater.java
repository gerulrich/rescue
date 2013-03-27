package net.cloudengine.widgets;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Display;

public class ViewUpdater implements Runnable {

	private ColumnViewer cv;

	public ViewUpdater(ColumnViewer cv) {
		this.cv = cv;
	}

	@Override
	public void run() {
		if (!this.cv.getControl().isDisposed()) {
			Display display = this.cv.getControl().getDisplay();
			display.syncExec(new Runnable() {
				public void run() {
					if(!cv.getControl().isDisposed())
						cv.refresh();
				}
			});
			display.timerExec(1000, this);
		}
	}
}