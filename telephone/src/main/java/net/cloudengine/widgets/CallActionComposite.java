package net.cloudengine.widgets;

import net.cloudengine.cti.Call;
import net.cloudengine.cti.CallsMonitor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.snippets.SquareButton;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class CallActionComposite extends Composite {

	private Listener hangupListener;
	private SquareButton hangupButton;
	public SquareButton holdButton;
	
	public CallActionComposite(final Call call, String phoneNumber, final CallsMonitor monitor, Composite parent, int style) {
		super(parent, style);
		
		hangupListener = new Listener() {
			public void handleEvent(Event event) {
				monitor.hungap(call);
			}
		};
		
		RowLayout layout = new RowLayout();
        layout.wrap = true;
        layout.type = SWT.HORIZONTAL;
        layout.center = true;
        layout.marginTop = 0;
        layout.marginBottom = 0;
		
        this.setLayout(layout);
		
		// Separador
		Label sep = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		sep.setLayoutData(new RowData(5,20));
		
		// boton de cortar
		Font font = new Font(Display.getCurrent(), "Arial",8,SWT.NORMAL); 
		
		hangupButton = new SquareButton(this, SWT.NONE);
		hangupButton.setText("Colgar");
		hangupButton.setFont(font);
		hangupButton.setRoundedCorners(true);
		hangupButton.setLayoutData(new RowData(50,24));
		hangupButton.addListener(SWT.Selection, hangupListener);
		
		holdButton = new SquareButton(this, SWT.NONE);
		holdButton.setText("Espera");
		holdButton.setFont(font);
		holdButton.setRoundedCorners(true);
		holdButton.setLayoutData(new RowData(50,24));
		
	    Label callerlabel = new Label(this, SWT.BORDER);
		if (call.getCalledId().equals(phoneNumber)) {
			callerlabel.setText(call.getCallerId());
		} else {
			callerlabel.setText(call.getCalledId());
		}
	    callerlabel.setLayoutData(new RowData(40,15));
	}

	@Override
	public void dispose() {
		hangupButton.removeListener(SWT.Selection, hangupListener);
		super.dispose();
	}

}
