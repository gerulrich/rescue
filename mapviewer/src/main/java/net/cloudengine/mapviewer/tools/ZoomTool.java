package net.cloudengine.mapviewer.tools;

import net.cloudengine.mapviewer.MapWidgetContext;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class ZoomTool extends AbstractTool {
	
	private Cursor openHand;
	private Cursor closedHand;
	private MapMouseListener mouseListener;
	
	public ZoomTool(MapWidgetContext context) {
		super(context, true);
		ImageData openHandimageData = new ImageData(ZoomTool.class.getResourceAsStream("open_hand.ico"));
    	ImageData closedHandimageData = new ImageData(ZoomTool.class.getResourceAsStream("closed_hand.ico"));
    	
    	openHand = new Cursor(Display.getCurrent(), openHandimageData, 0, 0);
    	closedHand = new Cursor(Display.getCurrent(), closedHandimageData, 0, 0);
    	
    	mouseListener = new MapMouseListener(context, this);
    	
	}
	
	@Override
	public ImageDescriptor getIcon() {
		return ImageDescriptor.createFromURL(ZoomTool.class.getResource("cursor_move.png"));
	}
	
	@Override
	public void activate() {
		changeCursorToOpenHand();
		getContext().getMap().addMouseListener(mouseListener);
		getContext().getMap().addMouseMoveListener(mouseListener);
		getContext().getMap().addMouseWheelListener(mouseListener);
		getContext().getMap().addMouseTrackListener(mouseListener);
	}

	@Override
	public void deactivate() {
		getContext().getMap().removeMouseListener(mouseListener);
		getContext().getMap().removeMouseMoveListener(mouseListener);
		getContext().getMap().removeMouseWheelListener(mouseListener);
		getContext().getMap().removeMouseTrackListener(mouseListener);
	}
	
//	public void registerListeners(MapWidget widget) {
//		widget.addMouseListener(mouseListener);
//		widget.addMouseMoveListener(mouseListener);
//		widget.addMouseWheelListener(mouseListener);
//		widget.addMouseTrackListener(mouseListener);
//	}
	
	public void changeCursorToOpenHand() {
		this.getContext().getMap().setCursor(openHand);
	}
	
	public void changeCursorToCloseHand() {
		this.getContext().getMap().setCursor(closedHand);
	}


	public void zoomIn(Point pivot) {
        if (getMap().getZoom() >= getMap().getTileServer().getMaxZoom())
            return;
        Point mapPosition = getMap().getMapPosition();
        int dx = pivot.x;
        int dy = pivot.y;
        getMap().setZoom(getMap().getZoom() + 1);
        getMap().setMapPosition(mapPosition.x * 2 + dx, mapPosition.y * 2 + dy);
        getMap().redraw();
    }

    public void zoomOut(Point pivot) {
        if (getMap().getZoom() <= 1)
            return;
        Point mapPosition = getMap().getMapPosition();
        int dx = pivot.x;
        int dy = pivot.y;
        getMap().setZoom(getMap().getZoom() - 1);
        getMap().setMapPosition((mapPosition.x - dx) / 2, (mapPosition.y - dy) / 2);
        getMap().redraw();
    }

}
