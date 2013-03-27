package net.cloudengine.widgets;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.cloudengine.new_.cti.model.QEntry;
import net.cloudengine.new_.cti.model.Queue;
import net.cloudengine.util.GuiUtil;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class QueueEntryLabelProvider implements ILabelProvider {

	private Image queueImg;
	private Image clock1Img;
	private Image clock2Img;
	private Image clock3Img;

	public QueueEntryLabelProvider() {
		queueImg = GuiUtil.getImage("status.png");
		clock1Img = GuiUtil.getImage("green_clock.png");
		clock2Img = GuiUtil.getImage("orange_clock.png");
		clock3Img = GuiUtil.getImage("red_clock.png");
	}
	
	@Override
	public void addListener(ILabelProviderListener arg0) {
		
	}

	@Override
	public void dispose() {
//		queueImg.dispose();
//		clock1Img.dispose();
//		clock2Img.dispose();
//		clock3Img.dispose();
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
		else if (element instanceof QEntry) {
			QEntry entry = (QEntry)element;
			long timeWait = timeWait(entry.getDate());
			if (timeWait < 60) {
				return clock1Img;
			} else if (timeWait < 120) {
				return clock2Img;
			} else {
				return clock3Img;		
			}			
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Queue) {
			Queue queue = (Queue) element;
			return queue.getName();
		} else if (element instanceof QEntry) {
			QEntry entry = (QEntry) element;
			String label = entry.getCallerId(); 
			return label+" - "+enqueueTime(entry.getDate());
		}
		return null;
	}
	
	public String enqueueTime(Date date) {
		Date current = new Date();
		long miliseconds = (current.getTime() - date.getTime());
		long hours =  TimeUnit.MILLISECONDS.toHours(miliseconds);
		long minutes =  TimeUnit.MILLISECONDS.toMinutes(miliseconds);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(miliseconds) % 60;
		return formatNumber(hours)+":"+formatNumber(minutes)+":"+formatNumber(seconds);
	}
	
	public String formatNumber(Long nro) {
		if (nro <= 9) {
			return "0"+nro;
		} else {
			return nro.toString();
		}
	}
	
	public long timeWait(Date date) {
		Date current = new Date();
		long miliseconds = (current.getTime() - date.getTime());
		return miliseconds/1000;
	}
	
}