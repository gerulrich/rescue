package net.cloudengine.widgets.panel;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/** Composite que contiene los botones
 * con los numeros para marcar.
 * @author German
 *
 */
public class KeyPad extends Composite {

	private Button nums[] = new Button[12];
	private Button back;
	private Button clr;
	
	private String keys[] = {
		"1", "2", "3",
		"4", "5", "6",
		"7", "8", "9",
		"*", "0", "#"
	};
	
	public KeyPad(Composite parent, int style) {
		super(parent, style);
		createGui(style);
	}
	
	private void createGui(int style) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.makeColumnsEqualWidth = true;
		this.setLayout(gridLayout);
		
		int i = 0;
		for (String key : keys) {
			nums[i] = new Button(this, style);
			nums[i].setText(key);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			nums[i].setLayoutData(gridData);
			i++;
		}
		
		back = new Button(this, style);
		back.setText("<");
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		back.setLayoutData(gridData);
		
		Label l = new Label(this, style);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		l.setLayoutData(gridData);
		
		clr = new Button(this, style);
		clr.setText("CLR");
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		clr.setLayoutData(gridData);
		
	}
	
	public Button[] getButtons() {
		return nums;
	}

}
