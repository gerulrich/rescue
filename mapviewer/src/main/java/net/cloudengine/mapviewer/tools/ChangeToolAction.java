package net.cloudengine.mapviewer.tools;

import org.eclipse.jface.action.Action;

public abstract class ChangeToolAction extends Action {
	
	private AbstractTool tool;
	
	public ChangeToolAction(String text, AbstractTool tool, boolean selected) {
		super(text, Action.AS_RADIO_BUTTON);
		this.setImageDescriptor(tool.getIcon());
		this.tool = tool;
//		this.setChecked(selected);
//		if (selected) {
//			tool.activate();
//		}
	}

	@Override
	public void run() {
		if (this.isChecked()) {
			tool.activate();
		} else {
			tool.deactivate();
		}
	}
	

}
