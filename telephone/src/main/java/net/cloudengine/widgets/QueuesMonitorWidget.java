package net.cloudengine.widgets;

import net.cloudengine.cti.CallsMonitor;
import net.cloudengine.pbx.PBXMonitor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.google.inject.Inject;

public class QueuesMonitorWidget {
	
	private PBXMonitor monitor;
	private CallsMonitor callsMonitor;

	@Inject
	public QueuesMonitorWidget(PBXMonitor monitor, CallsMonitor callsMonitor) {
		this.monitor = monitor;
		this.callsMonitor = callsMonitor;
	}
	
	public Control createControl(Composite parent) {
	    final TreeViewer tv = new TreeViewer(parent);
	    tv.setContentProvider(new QueueTreeContentProvider(tv,monitor, callsMonitor));
	    tv.setLabelProvider(new QueueLabelProvider());
	    tv.setInput("root");
	    tv.expandAll();
	    
	    tv.getTree().getDisplay().timerExec(1000, new ViewUpdater(tv));
	    
	    return parent;
	}
	
	class ViewUpdater implements Runnable {

		private TreeViewer tv;
		
		public ViewUpdater(TreeViewer tv) {
			this.tv = tv;
		}
		
		@Override
		public void run() {
			Display display = this.tv.getTree().getDisplay();
			display.syncExec (new Runnable () {
				public void run () {
					tv.refresh();
				}
			});
			display.timerExec(1000, this);
		}
		
	}

}
