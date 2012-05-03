package net.cloudengine.widgets;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.cloudengine.pbx.CTIQueue;
import net.cloudengine.pbx.CTIQueueEntry;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class QueueLabelProvider implements ILabelProvider {

	private Image queueImg;
	private Image clock1Img;
	private Image clock2Img;
	private Image clock3Img;

	public QueueLabelProvider() {
		try {
			queueImg = new Image(null,QueueLabelProvider.class.getResourceAsStream("status.png"));
			clock1Img = new Image(null,QueueLabelProvider.class.getResourceAsStream("green_clock.png"));
			clock2Img = new Image(null,QueueLabelProvider.class.getResourceAsStream("orange_clock.png"));
			clock3Img = new Image(null,QueueLabelProvider.class.getResourceAsStream("red_clock.png"));
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
		queueImg.dispose();
		clock1Img.dispose();
		clock2Img.dispose();
		clock3Img.dispose();
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
		if (element instanceof CTIQueue) {
			return queueImg;
		} else if (element instanceof CTIQueueEntry) {
			CTIQueueEntry entry = (CTIQueueEntry)element;
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
		if (element instanceof CTIQueue) {
			CTIQueue queue = (CTIQueue) element;
			return queue.getNumber();
		} else if (element instanceof CTIQueueEntry) {
			CTIQueueEntry entry = (CTIQueueEntry) element;
			String label = entry.getNumber(); 
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