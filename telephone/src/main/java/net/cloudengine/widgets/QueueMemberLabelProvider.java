package net.cloudengine.widgets;

import net.cloudengine.new_.cti.model.QMember;
import net.cloudengine.new_.cti.model.Queue;
import net.cloudengine.util.GuiUtil;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class QueueMemberLabelProvider implements ILabelProvider {

	private Image queueImg;
	private Image clock3Img;

	public QueueMemberLabelProvider() {
		queueImg = GuiUtil.getImage("status.png");
		clock3Img = GuiUtil.getImage("red_clock.png");
	}
	
	@Override
	public void addListener(ILabelProviderListener arg0) {
		
	}

	@Override
	public void dispose() {
//		queueImg.dispose();
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {

	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Queue) {
			return queueImg;
		} 
		else if (element instanceof QMember) {
			return clock3Img;		
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Queue) {
			Queue queue = (Queue) element;
			return queue.getName();
		} else if (element instanceof QMember) {
			QMember member = (QMember) element;
			return member.getNumber();
		}
		return null;
	}
}