package net.cloudengine.client.workbench;

import net.cloudengine.rpc.controller.auth.Context;
import net.cloudengine.rpc.controller.auth.UserModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.google.inject.Inject;

public class TestPage extends AbstractPage {
	
	private UserModel userModel;
	
	private Composite treeComp;
	private Composite previewComp;
	private Composite listAndContentComp;

	@Inject
	public TestPage(Context context) {
		this.userModel = context.getCurrentUser();
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

		treeComp = new Composite(vsf, SWT.NONE);

		listAndContentComp = new Composite(vsf, SWT.NONE);
		vsf.setWeights(new int[] { 25, 75 });

		GridLayout gl2 = new GridLayout();
		gl2.verticalSpacing = gl2.horizontalSpacing = 0;
		gl2.marginHeight = gl2.marginWidth = 0;
		listAndContentComp.setLayout(gl2);
		SashForm hvf = new SashForm(listAndContentComp, SWT.VERTICAL | SWT.SMOOTH);

		hvf.setLayoutData(gd5);
		Composite listComp = new Composite(hvf, SWT.BORDER);
		listComp.setLayout(new FillLayout());

		previewComp = new Composite(hvf, SWT.BORDER);
		previewComp.setLayout(new FillLayout());

		hvf.setWeights(new int[] { 50, 50 });
		
		initMailPreviewUI();
		updateMailBoxUI();
	}
	
	private void initMailPreviewUI() {
		new BrowserPanel(this.previewComp);
	}
	
	// Update the mailbox tree directory structure interface
	private void updateMailBoxUI() {
		// refresh the list based on currentAccount.
		Control[] ctrls = this.treeComp.getChildren();

		treeComp.setLayout(new FillLayout());
		if (ctrls.length != 0) {
			ctrls[0].dispose();
		}
		Tree acntTree = new Tree(treeComp, SWT.BORDER | SWT.SINGLE);
		
		TreeItem tac = new TreeItem(acntTree, SWT.NULL);
		tac.setText(userModel.getDisplayName() + " <" + userModel.getUsername() + ">");

		String []fds = {"Bandeja de entrada", "Mis asignaciones", "Mis atajos"}; 
		for (int i = 0; i < fds.length; i++) {
			TreeItem tit = new TreeItem(tac, SWT.NULL);
			tit.setText(fds[i]);
		}
		tac.setExpanded(true);
		treeComp.layout();
	}
	
	
	@Override
	protected void widgetDisposed(DisposeEvent e) {

	}



}
