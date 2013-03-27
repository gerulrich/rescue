package net.cloudengine.widgets;

import java.util.HashMap;
import java.util.Map;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.model.Call;
import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.QEntry;
import net.cloudengine.new_.cti.model.QMember;
import net.cloudengine.new_.cti.model.Queue;
import net.cloudengine.util.GuiUtil;

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

public class PhoneBar extends ContributionItem implements EventListener {
	
	private String phoneNumber = "-";
	private Composite composite;
	private Map<Call, CallActionComposite> callsActions = new HashMap<Call, CallActionComposite>();
	
	private Label imgLabelStatus;
	private Label textLabelStatus;
	
	private CoolItem labelItem;
	
	private Image imgConnected = GuiUtil.getImage("connected.png");
	private Image imgDisconnected = GuiUtil.getImage("disconnect.png");
	private Boolean connected = Boolean.FALSE;
	private EventProvider provider;
	
	public PhoneBar(EventProvider provider) {
		this.provider = provider;
		this.provider.addListener(this);
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
	    
	    if (connected) { // TODO
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
	public void dispose() {
		super.dispose();
		imgConnected.dispose();
		imgDisconnected.dispose();
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
				imgLabelStatus.setImage(imgDisconnected);
				textLabelStatus.setText("Desconectado");
				
				for (CallActionComposite comp : callsActions.values() ) {
					comp.dispose();
				}
				composite.layout();
				callsActions.clear();
			}
		});
	}

	@Override
	public void extensionChanged(Extension extension) { }

	@Override
	public void queueAdded(Queue queue) { }

	@Override
	public void queueMemberAdded(String queue, QMember member) { }

	@Override
	public void queueMemberRemoved(String queue, QMember member) { }

	@Override
	public void queueEntryAdded(String queue, QEntry entry) { }

	@Override
	public void newCall(final Call call) {
		if (call.getCalledId().equals(phoneNumber) || call.getCallerId().equals(phoneNumber)) {
			composite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					CallActionComposite llamadaComposite = new CallActionComposite(call, phoneNumber, provider,composite, SWT.NONE);
					callsActions.put(call, llamadaComposite);
					composite.layout();
				}
		    });
			provider.record(call);
		}
	}

	@Override
	public void hangupCall(final Call call) {
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
	public void extensionAdded(Extension extension) { }

	@Override
	public void queueEntryRemoved(String queue, QEntry entry) { }

	@Override
	public void changeCall(Call call) {
		
	}	

}
