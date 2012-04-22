package org.eclipse.swt.snippets;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

public class Item1 extends ContributionItem {

	public Item1(String id) {
		super(id);
	}

	@Override
	public void fill(Composite parent) {
		// TODO Auto-generated method stub
		super.fill(parent);
	}

	@Override
	public void fill(CoolBar parent, int index) {

	    Label label = new Label(parent, SWT.READ_ONLY);
	    label.setText("Presente");
	    label.pack();
	    
	    CoolItem labelItem = new CoolItem(parent, SWT.SEPARATOR);
	    labelItem.setControl(label);
	    labelItem.setPreferredSize(70, label.getBounds().height);
		
//		Label label= new Label(parent,SWT.NONE);
//		label.setText("LABEL");
		
		
//		super.fill(parent, index);
		
	}

	@Override
	public void fill(Menu menu, int index) {
		// TODO Auto-generated method stub
		super.fill(menu, index);
	}

	@Override
	public void fill(ToolBar parent, int index) {
		// TODO Auto-generated method stub
		super.fill(parent, index);
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	@Override
	public IContributionManager getParent() {
		// TODO Auto-generated method stub
		return super.getParent();
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return super.isDirty();
	}

	@Override
	public boolean isDynamic() {
		// TODO Auto-generated method stub
		return super.isDynamic();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return super.isEnabled();
	}

	@Override
	public boolean isGroupMarker() {
		// TODO Auto-generated method stub
		return super.isGroupMarker();
	}

	@Override
	public boolean isSeparator() {
		// TODO Auto-generated method stub
		return super.isSeparator();
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return super.isVisible();
	}

	@Override
	public void saveWidgetState() {
		// TODO Auto-generated method stub
		super.saveWidgetState();
	}

	@Override
	public void setId(String itemId) {
		// TODO Auto-generated method stub
		super.setId(itemId);
	}

	@Override
	public void setParent(IContributionManager parent) {
		// TODO Auto-generated method stub
		super.setParent(parent);
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		super.setVisible(visible);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}

	@Override
	public void update(String id) {
		// TODO Auto-generated method stub
		super.update(id);
	}

	// public void fill(Composite parent) {
	//
	// Label label= new Label(parent,SWT.NONE);
	// label.setText("LABEL");
	// }

}