package net.cloudengine.widgets;

import net.cloudengine.new_.cti.EventProvider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;

//import com.google.inject.Inject;

public class CallWatcher {
	
	private EventProvider provider;
	
//	@Inject
	public CallWatcher(EventProvider provider) {
		this.provider = provider;
	}
	
	
	public Control createControl(Composite parent) {
		Composite wrap = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		wrap.setLayout(layout);
		
		TitleControl title = new TitleControl(wrap);
		title.setText("Llamadas en curso");
		title.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		
		final TableViewer v = new TableViewer(wrap, SWT.BORDER | SWT.FULL_SELECTION);
		v.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		v.setLabelProvider(new CallLabelProvider());
		v.setContentProvider(new CallsContentProvider(provider, v));
		
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
}
