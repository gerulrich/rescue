package net.cloudengine.widgets;

import net.cloudengine.cti.CallsMonitor;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;

import com.google.inject.Inject;

public class CallWatcher {
	
	private CallsMonitor monitor;
	
	@Inject
	public CallWatcher(CallsMonitor monitor) {
		this.monitor = monitor;
	}
	
	
	public Control createControl(Composite parent) {
		
		final TableViewer v = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		v.setLabelProvider(new CallLabelProvider());
		v.setContentProvider(new CallsContentProvider(monitor, v));
		
		TableColumn column = new TableColumn(v.getTable(),SWT.NONE);
		column.setWidth(80);
		column.setText("CallerId");
		
		column = new TableColumn(v.getTable() , SWT.NONE);
		column.setWidth(80);
		column.setText("CalledId");
		
		column = new TableColumn(v.getTable() , SWT.NONE);
		column.setWidth(80);
		column.setText("Estado");
		
		column = new TableColumn(v.getTable() , SWT.NONE);
		column.setWidth(80);
		column.setText("Duracion");
		
		v.setInput("root");
		v.getTable().setLinesVisible(true);
		v.getTable().setHeaderVisible(true);
		
		v.getTable().getDisplay().timerExec(1000, new ViewUpdater(v));
		
		return parent;
	}
	
	class ViewUpdater implements Runnable {

		private TableViewer tv;
		
		public ViewUpdater(TableViewer tv) {
			this.tv = tv;
		}
		
		@Override
		public void run() {
			Display display = this.tv.getTable().getDisplay();
			display.syncExec (new Runnable () {
				public void run () {
					tv.refresh();
				}
			});
			display.timerExec(1000, this);
		}
		
	}

}
