package net.cloudengine.widgets;

import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.Status;
import net.cloudengine.util.GuiUtil;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class MyLabelProvider implements ILabelProvider {

	private Image online;
	private Image busy;
	private Image offline;
	private Image groupOnline;
	private Image groupOffline;
	
	public MyLabelProvider() {
		online = GuiUtil.getImage("status.png");
		busy = GuiUtil.getImage("busy.png");
		offline = GuiUtil.getImage("offline.png");
		groupOnline = GuiUtil.getImage("network-online.png");
		groupOffline = GuiUtil.getImage("network-offline.png");
	}	

	@Override
	public void dispose() {
//		busy.dispose();
//		offline.dispose();
//		groupOnline.dispose();
//		groupOffline.dispose();
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) { 
		return false; 
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
	}
	
	@Override
	public void removeListener(ILabelProviderListener arg0) { 
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Extension) {
			Extension ext = (Extension) element;
			if (ext.getStatus().equals(Status.UNAVAILABLE))
				return offline;
			else {
				
				if (ext.getStatus().equals(Status.INUSE)) {
					return busy;
				} else {
					return online;
				}
			}
		} else if (element instanceof String) {
			String group = (String) element;
			if ("Conectados".endsWith(group)) { // FIXME sacar la constante
				return groupOnline;
			} else {
				return groupOffline;
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof String) {
			return element.toString();
		} else if (element instanceof Extension) {
			return getText((Extension)element);
		}
		return null;
	}
	
	private String getText(Extension extension) {
		String additinalInfo = null;
		switch (extension.getStatus()) {
			case RINGING: additinalInfo = "Sonando"; break;
		}
		if (additinalInfo != null)
			return extension.getNumber()+" ("+extension.getType()+")"+" - "+additinalInfo;
		else
			return extension.getNumber()+" ("+extension.getType()+")";
	}
}