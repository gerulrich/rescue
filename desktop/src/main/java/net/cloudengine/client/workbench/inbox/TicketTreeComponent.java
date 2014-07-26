package net.cloudengine.client.workbench.inbox;

import net.cloudengine.rpc.controller.auth.UserModel;
import net.cloudengine.widgets.panel.PhonePanel;
import net.cloudengine.widgets.sound.Settings;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TicketTreeComponent extends Composite {

	private TreeItem inboxTree;
//	private UserModel userModel;
	
	public TicketTreeComponent(Composite parent, int style, UserModel userModel, boolean sipEnabled) {
		super(parent, style);
		
		if (sipEnabled && StringUtils.isNotEmpty(userModel.getAgentNumber())) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			gridLayout.marginHeight = 0;
			gridLayout.marginWidth = 0;
			gridLayout.makeColumnsEqualWidth = true;
			this.setLayout(gridLayout);
		} else {
			this.setLayout(new FillLayout());
		}
			
		Tree acntTree = new Tree(this, SWT.BORDER);

		if (sipEnabled && StringUtils.isNotEmpty(userModel.getAgentNumber())) {
			
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.heightHint = 150;
			acntTree.setLayoutData(gridData);
			
			
			Settings settings = new Settings(userModel.getAgentNumber(), userModel.getAgentPassword(), 
					"localhost", 5060);
			PhonePanel phonePanel = new PhonePanel(settings, null, this, SWT.NONE);
			gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			phonePanel.setLayoutData(gridData);
			
		}
		
		inboxTree = new TreeItem(acntTree, SWT.NULL);
		inboxTree.setText(userModel.getUsername());

		inboxTree.setExpanded(true);
		this.layout();		
		
	}
	
	

}
