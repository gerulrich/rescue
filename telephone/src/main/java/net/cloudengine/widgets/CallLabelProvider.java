package net.cloudengine.widgets;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import net.cloudengine.cti.Call;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * {@link LabelProvider} que representa las llamadas en una tabla.
 * @author German Ulrich
 *
 */
public class CallLabelProvider extends LabelProvider implements ITableLabelProvider {

    private Image greenIcon;
    private Image orangeIcon;
	
	public CallLabelProvider() {
		this.greenIcon = new Image(null, 14, 14);
		GC gc = new GC(greenIcon);
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
	    gc.fillRectangle(greenIcon.getBounds());
	    gc.dispose();
	    
	    Color orange = new Color(Display.getCurrent(), 255, 128, 0);
	    this.orangeIcon = new Image(null, 14, 14);
		gc = new GC(orangeIcon);
		gc.setBackground(orange);
	    gc.fillRectangle(orangeIcon.getBounds());
	    gc.dispose();
	    orange.dispose();
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		Call call = (Call) element;
		if (columnIndex == 0) {
			if (call.isHold()) {
				return orangeIcon;
			}
			return greenIcon;
		}
		else {
			return null;
		}
	}

	public String getColumnText(Object element, int columnIndex) {
		Call call = (Call) element;
		switch (columnIndex) {
			case 0: return call.getCallerId();
			case 1: return call.getCalledId();
			case 2: 
				if (call.isHold()) {
					return "En espera";
				} else {
					return "Activa";
				}
			case 3: return callTime(call.creationDate());
		}
		return null;
	}
	
	public String callTime(Date date) {
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

	@Override
	public void dispose() {
		greenIcon.dispose();
		orangeIcon.dispose();
		super.dispose();
	}	
}