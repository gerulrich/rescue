package net.cloudengine.mapviewer.tools.selection;

import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.util.PointD;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;

public class SelectionMouseListener implements MouseListener, MouseMoveListener, MouseTrackListener {
    
	private Point mouseCoords = new Point(0, 0);
    private Point downCoords;
    private Point downPosition;
    private SelectionTool tool;
    
    
	
	private MapWidgetContext context;
	
    public SelectionMouseListener(MapWidgetContext context, SelectionTool tool) {
		super();
		this.context = context;
		this.tool = tool;
	}

	public void mouseEnter(MouseEvent e) {
		this.context.getMap().forceFocus();
    }
    
    public void mouseExit(MouseEvent e) {
    }

    public void mouseHover(MouseEvent e) {
    	
    }
    
    public void mouseDoubleClick(MouseEvent e) {
    }
    
    private Point downPoint;
    private Point currPoint;
    
    public void mouseDown(MouseEvent e) {
   		
   		downPoint = new Point(e.x, e.y);
   		tool.getRenderer().setDownPoint(downPoint);
    }

    public void mouseUp(MouseEvent e) {
    	
    	if (downPoint != null && currPoint != null) {
    		
    		
    		int x1 = Math.min(downPoint.x, currPoint.x);
			int y1 = Math.min(downPoint.y, currPoint.y);
			
			int x2 = Math.max(downPoint.x, currPoint.x);
			int y2 = Math.max(downPoint.y, currPoint.y);
    		
    		Point position1 = new Point(
    				context.getMap().getMapPosition().x + x1, 
    				context.getMap().getMapPosition().y + y1);
    		PointD p1 = context.getMap().getLongitudeLatitude(position1);
    		
    		Point position2 = new Point(
    				context.getMap().getMapPosition().x + x2, 
    				context.getMap().getMapPosition().y + y2);
    		PointD p2 = context.getMap().getLongitudeLatitude(position2);
    		
    		tool.selectItems(p1.x, p1.y, p2.x,p2.y, (e.stateMask & SWT.CTRL) != 0);
    		
    	} else if (downPoint != null && currPoint == null) {
    		
    		Point position = new Point(
    				context.getMap().getMapPosition().x + e.x, 
    				context.getMap().getMapPosition().y + e.y);
    		PointD p = context.getMap().getLongitudeLatitude(position);
    		tool.selectItems(p.y, p.x, (e.stateMask & SWT.CTRL) != 0);
    	}    	
    	
    	
    	tool.getRenderer().setCursorPoint(null);
    	tool.getRenderer().setDownPoint(null);
    	tool.getContext().getMap().redraw();
    	downPoint = null;
    	currPoint = null;
    }

    @Override
    public void mouseMove(MouseEvent e) {
    	handlePosition(e);
        handleDrag(e);
    }
    
    public void mouseScrolled(MouseEvent e) {
    }

    private void handlePosition(MouseEvent e) {
    	tool.getRenderer().setCursorPoint(new Point(e.x, e.y));
    	tool.getContext().getMap().redraw();
    }

    private void handleDrag(MouseEvent e) {
        if (downPoint != null) {
        	currPoint = new Point(e.x, e.y);
        }
    	if (downCoords != null) {
            int tx = downCoords.x - e.x;
            int ty = downCoords.y - e.y;
            this.context.getMap().setMapPosition(downPosition.x + tx, downPosition.y + ty);
            this.context.getMap().redraw();
        }
    }
}