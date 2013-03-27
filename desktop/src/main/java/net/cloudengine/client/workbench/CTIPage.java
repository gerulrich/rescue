package net.cloudengine.client.workbench;

import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.widgets.CallWatcher;
import net.cloudengine.widgets.PhonesMonitorWidget;
import net.cloudengine.widgets.QueuesEntryWidget;
import net.cloudengine.widgets.QueuesMemberWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class CTIPage extends AbstractPage {
	
	private Composite extComp;
	private Composite callsComp;
	private Composite queuesEntryComp;
	private Composite queuesMemberComp;
	
	private CallWatcher callWatcher;
	private EventProvider provider;
	
	public CTIPage(EventProvider provider) {
		this.provider = provider;
	}

	@Override
	protected void initContent(PageContainer container, Composite composite) {
		GridLayout gl1 = new GridLayout();
		gl1.verticalSpacing = gl1.horizontalSpacing = 1;
		gl1.marginHeight = gl1.marginWidth = 2;
		composite.setLayout(gl1);
		
		
		SashForm vsf = new SashForm(composite, SWT.HORIZONTAL | SWT.SMOOTH);

		GridData gd5 = new GridData();
		gd5.horizontalAlignment = SWT.FILL;
		gd5.verticalAlignment = SWT.FILL;
		gd5.grabExcessHorizontalSpace = true;
		gd5.grabExcessVerticalSpace = true;
		vsf.setLayoutData(gd5);

		extComp = new Composite(vsf, SWT.NONE);
		extComp.setLayout(new FillLayout());
		
		callsComp = new Composite(vsf, SWT.NONE);
		callsComp.setLayout(new FillLayout());
		
		callWatcher = new CallWatcher(provider);
		callWatcher.createControl(callsComp);
		
		PhonesMonitorWidget widget = new PhonesMonitorWidget(provider);
		widget.createControl(extComp);
		
		SashForm vsfQueues = new SashForm(vsf, SWT.VERTICAL | SWT.SMOOTH);
		
		queuesEntryComp = new Composite(vsfQueues, SWT.NONE);
		queuesEntryComp.setLayout(new FillLayout());
		
		QueuesEntryWidget queueWidget = new QueuesEntryWidget(provider);
		queueWidget.createControl(queuesEntryComp);
		
		queuesMemberComp = new Composite(vsfQueues, SWT.NONE);
		queuesMemberComp.setLayout(new FillLayout());
		
		QueuesMemberWidget queueMemberWidget = new QueuesMemberWidget(provider);
		queueMemberWidget.createControl(queuesMemberComp);
		
		vsf.setWeights(new int[] { 20, 40, 40 });
		vsfQueues.setWeights(new int[] { 50, 50 });
	}
	
	
	@Override
	protected void widgetDisposed(DisposeEvent e) {

	}

}
