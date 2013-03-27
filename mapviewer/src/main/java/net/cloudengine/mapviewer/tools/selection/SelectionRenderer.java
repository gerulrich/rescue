package net.cloudengine.mapviewer.tools.selection;

import net.cloudengine.mapviewer.tools.renderer.Renderer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public class SelectionRenderer implements Renderer {

	private Point downPoint;
	private Point cursorPoint;
	
	public Point getDownPoint() {
		return downPoint;
	}

	public void setDownPoint(Point downPoint) {
		this.downPoint = downPoint;
	}

	public Point getCursorPoint() {
		return cursorPoint;
	}

	public void setCursorPoint(Point cursorPoint) {
		this.cursorPoint = cursorPoint;
	}

	@Override
	public void paint(GC gc) {
		
		if (downPoint != null && cursorPoint != null) {
			gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineStyle(1);
			gc.setLineWidth(1);
			int x1 = Math.min(downPoint.x, cursorPoint.x);
			int y1 = Math.min(downPoint.y, cursorPoint.y);
			
			int x2 = Math.abs(downPoint.x - cursorPoint.x);
			int y2 = Math.abs(downPoint.y - cursorPoint.y);
			gc.drawRectangle(x1,y1,x2,y2);
			
		}
	}

	@Override
	public void clear() {

	}

	@Override
	public void dispose() {

	}

}
