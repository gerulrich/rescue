package net.cloudengine.widgets.panel;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class AccionsControl extends Composite {

	private Button buttons[] = new Button[6];
	private String labels[] = {"TR", "HD", "RE", "AA", "DND", "MON"}; 
	
	public AccionsControl(Composite parent, int style) {
		super(parent, style);
		createGui(style);
	}
	
	private void createGui(int style) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gridLayout.makeColumnsEqualWidth = true;
		this.setLayout(gridLayout);
		
		for(int i=0; i < 6; i++) {
//			if (i < 5)
//				buttons[i] = new Button(this, SWT.TOGGLE | style);
//			else
				buttons[i] = new Button(this, style);
//			buttons[i].setText(labels[i]);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			buttons[i].setLayoutData(gridData);
		}
		
		for(int i=0; i < 6; i++) {
			Label l = new Label(this, style);
			l.setText(labels[i]);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.CENTER;
			gridData.grabExcessHorizontalSpace = true;
			l.setLayoutData(gridData);
		}
	}
	
	public Button[] getButtons() {
		return buttons;
	}

}
