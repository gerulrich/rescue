package net.cloudengine.client.workbench;

import net.cloudengine.client.workbench.inbox.FolderSource;
import net.cloudengine.client.workbench.inbox.TicketFolderSource;
import net.cloudengine.client.workbench.inbox.TicketListComponent;
import net.cloudengine.client.workbench.inbox.TicketTreeComponent;
import net.cloudengine.rpc.controller.auth.Context;
import net.cloudengine.rpc.controller.auth.UserModel;
import net.cloudengine.rpc.controller.config.PropertyController;
import net.cloudengine.rpc.controller.ticket.InboxController;
import net.cloudengine.rpc.model.TicketViewModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;

public class InboxPage extends AbstractPage {
	
	private UserModel userModel;
	
//	private Composite treeComp;
	private Composite previewComp;
	private Composite listAndContentComp;
	private PropertyController propertyController;
	private InboxController inboxController;
//	private TreeItem inboxTree;
	
	
	private TicketListComponent ticketListComponent;
	private TicketTreeComponent ticketTreeComponent;
	
	@Inject
	public InboxPage(Context context, PropertyController propertyController, InboxController inboxController) {
		this.userModel = context.getCurrentUser();
		this.propertyController = propertyController;
		this.inboxController = inboxController;
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

		// arbol de cartepas - inbox - panel de telefono
//		treeComp = new Composite(vsf, SWT.NONE);
		boolean sipEnabled = propertyController.getProperty("sip.enabled").getValue(Boolean.class);
		ticketTreeComponent = new TicketTreeComponent(vsf, SWT.NONE, userModel, sipEnabled);

		
		//
		listAndContentComp = new Composite(vsf, SWT.NONE);
		vsf.setWeights(new int[] { 25, 75 });

		GridLayout gl2 = new GridLayout();
		gl2.verticalSpacing = gl2.horizontalSpacing = 0;
		gl2.marginHeight = gl2.marginWidth = 0;
		listAndContentComp.setLayout(gl2);
		SashForm hvf = new SashForm(listAndContentComp, SWT.VERTICAL | SWT.SMOOTH);

		hvf.setLayoutData(gd5);

		// Lista de tickets
		
		FolderSource<TicketViewModel> folderSource = new TicketFolderSource(inboxController);
		ticketListComponent = new TicketListComponent(hvf, SWT.BORDER, folderSource);
		
		///////////////////

		previewComp = new Composite(hvf, SWT.BORDER);
		previewComp.setLayout(new FillLayout());

		hvf.setWeights(new int[] { 50, 50 });
		
		initPreviewTicketUI();
//		updateMailBoxUI();

	}
	
	private void initPreviewTicketUI() {
		new BrowserPanel(this.previewComp);
	}
	
	@Override
	protected void widgetDisposed(DisposeEvent e) {
		ticketListComponent.dispose();
		ticketTreeComponent.dispose();
	}

}
