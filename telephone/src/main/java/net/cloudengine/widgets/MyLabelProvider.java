package net.cloudengine.widgets;

import net.cloudengine.pbx.Group;
import net.cloudengine.pbx.PhoneExt;
import net.cloudengine.pbx.Status;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class MyLabelProvider implements ILabelProvider {

	Image online;
	Image busy;
	Image offline;
	Image groupOnline;
	Image groupOffline;
	
	public MyLabelProvider() {
		try {
			online = new Image(null,MyLabelProvider.class.getResourceAsStream("status.png"));
			busy = new Image(null,MyLabelProvider.class.getResourceAsStream("busy.png"));
			offline = new Image(null,MyLabelProvider.class.getResourceAsStream("offline.png"));
			groupOnline = new Image(null,MyLabelProvider.class.getResourceAsStream("network-online.png"));
			groupOffline = new Image(null,MyLabelProvider.class.getResourceAsStream("network-offline.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		busy.dispose();
		offline.dispose();
		groupOnline.dispose();
		groupOffline.dispose();
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof PhoneExt) {
			PhoneExt phone = (PhoneExt) element;
			if (phone.getStatus().equals(Status.UNAVAILABLE))
				return offline;
			else {
				
				if (phone.getStatus().equals(Status.INUSE)) {
					return busy;
				} else {
					return online;
				}
			}
		} else if (element instanceof Group) {
			Group group = (Group) element;
			if (Status.UNAVAILABLE.equals(group.getStatus())) {
				return groupOffline;
			} else {
				return groupOnline;
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Group) {
			return getText((Group)element);
		} else if (element instanceof PhoneExt) {
			return getText((PhoneExt)element);
		}
		return null;
	}
	
	private String getText(Group group) {
		return group.getName();
	}
	
	private String getText(PhoneExt phone) {
		String additinalInfo = null;
		switch (phone.getStatus()) {
			case RINGING: additinalInfo = "Sonando"; break;
		}
		if (additinalInfo != null)
			return phone.getNumber()+" ("+phone.getType()+")"+" - "+additinalInfo;
		else
			return phone.getNumber()+" ("+phone.getType()+")";
	}

}