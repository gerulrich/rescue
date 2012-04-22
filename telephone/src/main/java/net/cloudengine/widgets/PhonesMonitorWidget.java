package net.cloudengine.widgets;

import net.cloudengine.pbx.PBXMonitor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.google.inject.Inject;

public class PhonesMonitorWidget {
	
	private PBXMonitor monitor;

	@Inject
	public PhonesMonitorWidget(PBXMonitor monitor) {
		this.monitor = monitor;
	}
	
	public Control createControl(Composite parent) {
	    final TreeViewer tv = new TreeViewer(parent);
	    tv.setContentProvider(new MyTreeContentProvider(tv,monitor));
	    tv.setLabelProvider(new MyLabelProvider());
	    tv.setInput("root");
	    tv.expandAll();
	    return parent;
	}	

}
