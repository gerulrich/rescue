package net.cloudengine.widgets;

import java.util.HashMap;
import java.util.Map;

import net.cloudengine.cti.Call;
import net.cloudengine.cti.CallListener;
import net.cloudengine.cti.CallsMonitor;
import net.cloudengine.cti.Connection;
import net.cloudengine.cti.ConnectionListener;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.google.inject.Inject;

public class PhoneBar extends ContributionItem implements CallListener, ConnectionListener {
	
	private String phoneNumber = "-";
	private Composite composite;
	private CallsMonitor monitor;
	private Map<Call, CallActionComposite> callsActions = new HashMap<Call, CallActionComposite>();
	
	private Label imgLabelStatus;
	private Label textLabelStatus;
	
	private CoolItem labelItem;
	
	private Image imgConnected = new Image(null,PhoneBar.class.getResourceAsStream("connect_yes.png"));
	private Image imgDisconnected = new Image(null,PhoneBar.class.getResourceAsStream("connect_no.png"));
	
	private Boolean connected = Boolean.FALSE;
	
	@Inject
	public PhoneBar(CallsMonitor monitor, Connection connection) {
		monitor.addListener(this);
		this.monitor = monitor;
		connection.register(this);
	}
	
	@Override
	public void fill(CoolBar parent, int index) {
		
		RowLayout layout = new RowLayout();
        layout.wrap = true;
        layout.marginBottom = 0;
        layout.type = SWT.HORIZONTAL;
        layout.center = true;
        layout.marginTop = 0;
        layout.marginBottom = 0;
		
		composite = new Composite(parent, SWT.NONE);
        composite.setLayout(layout);
		
        imgLabelStatus = new Label(composite, SWT.NONE);
	    imgLabelStatus.setImage(imgDisconnected);
	    imgLabelStatus.setLayoutData(new RowData(30,30));
	    
	    textLabelStatus = new Label(composite, SWT.NONE);
	    textLabelStatus.setText("Desconectado");
	    textLabelStatus.setLayoutData(new RowData(75,15));
	    
	    labelItem = new CoolItem(parent, SWT.NONE);
	    labelItem.setControl(composite);
	    labelItem.setPreferredSize(imgLabelStatus.getBounds().width, imgLabelStatus.getBounds().height);
	    
	    if (connected) {
	    	this.onConnect();
	    }
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		if (composite != null) {
			this.composite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					if (connected) {
						onConnect();
					}
				}
			});
		}
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public void update() {
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				composite.layout();
			}
	    });
	    super.update();
	}

	@Override
	public void onNewCall(final Call call) {
		if (call.getCalledId().equals(phoneNumber) || call.getCallerId().equals(phoneNumber)) {
			composite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					CallActionComposite llamadaComposite = new CallActionComposite(call, phoneNumber, monitor, composite, SWT.NONE);
					callsActions.put(call, llamadaComposite);
					composite.layout();
				}
		    });
			
			monitor.record(call);
		}
	}

	@Override
	public void onCallFinish(final Call call) {
		if (callsActions.containsKey(call)) {
			composite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					callsActions.get(call).dispose();
					callsActions.remove(call);
					composite.layout();
				}
			});
		}
	}

	@Override
	public void onChangeState(Call call) {
		if (callsActions.containsKey(call)) {
			CallActionComposite ca = callsActions.get(call);
			if (call.isHold()) {
				ca.holdButton.setText("Recuep.");
			} else {
				ca.holdButton.setText("Espera");
			}
		}
	}

	@Override
	public void onConnect() {
		connected = true;
		if (composite != null) {
			composite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					imgLabelStatus.setImage(imgConnected);
					textLabelStatus.setText("INT: "+phoneNumber);
				}
			});
		}
	}

	@Override
	public void onDisconnect() {
		connected = false;
		composite.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				imgLabelStatus.setImage(imgConnected);
				textLabelStatus.setText("Desconectado");
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();
		imgConnected.dispose();
		imgDisconnected.dispose();
	}

	@Override
	public void onQueueCall(Call call, String queue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDequeueCall(Call call, String queue) {
		// TODO Auto-generated method stub
		
	}

}
