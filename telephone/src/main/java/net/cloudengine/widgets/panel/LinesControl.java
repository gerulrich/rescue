package net.cloudengine.widgets.panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Composite con los botones para realizar
 * las diferentes acciones con el telefono.
 * @author German
 *
 */
public class LinesControl extends Composite {

	private Button lines[] = new Button[6];
	
	public LinesControl(Composite parent, int style) {
		super(parent, style);
		createGui(style);
	}
	
	private void createGui(int style) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gridLayout.makeColumnsEqualWidth = true;
		this.setLayout(gridLayout);
		
		for(int i=0; i < 6; i++) {
			lines[i] = new Button(this, SWT.TOGGLE | style);
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			lines[i].setLayoutData(gridData);
		}
		
		for(int i=0; i < 6; i++) {
			Label l = new Label(this, style);
			l.setText("L"+(i+1));
			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.CENTER;
			gridData.grabExcessHorizontalSpace = true;
			l.setLayoutData(gridData);			
		}
	}
	
	public Button[] getButtons() {
		return lines;
	}
	
	public void unselectAll() {
		for (int a = 0; a < 6; a++) {
			final Button b = lines[a]; 
			this.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					b.setSelection(false);
				}
			});	
		}
	}
	
	public void selectLine(int line) {
		final Button b = lines[line];
		this.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				b.setSelection(true);
			}
		});
	}


}