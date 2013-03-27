package net.cloudengine.widgets;

import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.model.Call;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class CallActionComposite extends Composite {

	private Listener hangupListener;
	private Button hangupButton;
	private Button holdButton;
	private EventProvider provider;
	
	public CallActionComposite(final Call call, String phoneNumber, EventProvider p, Composite parent, int style) {
		super(parent, style);
		this.provider = p;
		hangupListener = new Listener() {
			public void handleEvent(Event event) {
				provider.hangup(call);
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
		
		hangupButton = new Button(this, SWT.NONE);
		hangupButton.setText("Colgar");
		hangupButton.setLayoutData(new RowData(50,24));
		hangupButton.addListener(SWT.Selection, hangupListener);
		
		holdButton = new Button(this, SWT.NONE);
		holdButton.setText("Espera");
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
