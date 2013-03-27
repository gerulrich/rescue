package net.cloudengine.mapviewer.tools;

import net.cloudengine.mapviewer.MapWidgetContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;

public class MapMouseListener implements MouseListener, MouseWheelListener, MouseMoveListener, MouseTrackListener {
    
	private Point mouseCoords = new Point(0, 0);
    private Point downCoords;
    private Point downPosition;
    private ZoomTool tool;
	
	private MapWidgetContext context;
	
    public MapMouseListener(MapWidgetContext context, ZoomTool tool) {
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
        if (e.button == 1) 
        	tool.zoomIn(new Point(mouseCoords.x, mouseCoords.y));
        else if (e.button == 3)
        	tool.zoomOut(new Point(mouseCoords.x, mouseCoords.y));
    }
    public void mouseDown(MouseEvent e) {
    	this.tool.changeCursorToCloseHand();
    	
    	if (e.button == 1 && (e.stateMask & SWT.CTRL) != 0) {
    		// FIXME
    		this.context.getMap().setCenterPosition(this.context.getMap().getCursorPosition());
    		this.context.getMap().redraw();
        }
        if (e.button == 1) {
            downCoords = new Point(e.x, e.y);
            downPosition = this.context.getMap().getMapPosition();
        }
        
//        if (e.button == 1 && (e.stateMask & SWT.SHIFT) != 0 ) {
//        	System.out.println("Click");
//        	try {
//            	Point p = new Point(mouseCoords.x, mouseCoords.y);
//            	for(ICanvasLayer layer:  mapWidget.getLayers()) {
//            		if (layer instanceof ISelectableLayer) {
//            			
//            			((ISelectableLayer)layer).selectObjects(p.x, p.y);
//            		}
//            	}
//            	MapWidget.this.redraw();
//            } catch (Exception ex) {
//            	ex.printStackTrace();
//            }
//        }
        
    }
    public void mouseUp(MouseEvent e) {
    	this.tool.changeCursorToOpenHand();
        if (e.count == 1) {
            handleDrag(e);
        }
        downCoords = null;
        downPosition = null;
    }
    
    public void mouseMove(MouseEvent e) {
        handlePosition(e);
        handleDrag(e);
    }
    public void mouseScrolled(MouseEvent e) {
        if (e.count == 3)
        	tool.zoomIn(new Point(mouseCoords.x, mouseCoords.y));
        else if (e.count == -3)
        	tool.zoomOut(new Point(mouseCoords.x, mouseCoords.y));
    }
    
    private void handlePosition(MouseEvent e) {
        mouseCoords = new Point(e.x, e.y);
        context.getMap().setMouseCoords(mouseCoords);
    }

    private void handleDrag(MouseEvent e) {
        if (downCoords != null) {
            int tx = downCoords.x - e.x;
            int ty = downCoords.y - e.y;
            this.context.getMap().setMapPosition(downPosition.x + tx, downPosition.y + ty);
            this.context.getMap().redraw();
        }
    }
}