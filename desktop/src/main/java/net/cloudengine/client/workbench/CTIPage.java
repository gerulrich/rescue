package net.cloudengine.client.workbench;

import net.cloudengine.cti.CallsMonitor;
import net.cloudengine.pbx.PBXMonitor;
import net.cloudengine.widgets.CallWatcher;
import net.cloudengine.widgets.PhonesMonitorWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;

public class CTIPage extends AbstractPage {
	
	private Composite extComp;
	private Composite callsComp;
	private Composite queuesComp;
	
	
	private CallsMonitor monitor;
	private PBXMonitor pbxMonitor;
	
	private CallWatcher callWatcher;
	
	@Inject
	public CTIPage(CallsMonitor monitor, PBXMonitor pbxMonitor) {
		this.monitor = monitor;
		this.pbxMonitor = pbxMonitor;
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
		
		callWatcher = new CallWatcher(monitor);
		callWatcher.createControl(callsComp);
		
		PhonesMonitorWidget widget = new PhonesMonitorWidget(pbxMonitor);
		widget.createControl(extComp);
		
		
		
		queuesComp = new Composite(vsf, SWT.NONE);
		
		vsf.setWeights(new int[] { 20, 40, 40 });
	}
	
	
	@Override
	protected void widgetDisposed(DisposeEvent e) {

	}



}
