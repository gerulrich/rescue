package net.cloudengine.widgets;

import net.cloudengine.new_.cti.EventProvider;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class QueuesMemberWidget {
	
	private EventProvider provider;
	
	public QueuesMemberWidget(EventProvider provider) {
		this.provider = provider;
	}
	
	public Control createControl(Composite parent) {
		
		 Composite wrap = new Composite(parent, SWT.NONE);
		 GridLayout layout = new GridLayout(1, false);
		 layout.marginWidth = layout.marginHeight = 0;
		 layout.horizontalSpacing = layout.verticalSpacing = 0;
		 wrap.setLayout(layout);
		
		 TitleControl title = new TitleControl(wrap);
		 title.setText("Agentes en cola");
		 title.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		 
		 final TreeViewer tv = new TreeViewer(wrap);
		 tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		 tv.setContentProvider(new QueueMemberContentProvider(tv, provider));
		 tv.setLabelProvider(new QueueMemberLabelProvider());
		 tv.setInput("root");
		 tv.expandAll();
		 tv.getTree().getDisplay().timerExec(1000, new ViewUpdater(tv));
		 return parent;
	}
}
