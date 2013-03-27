package net.cloudengine.client.xmap;

import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.Symbolizer;
import net.cloudengine.mapviewer.util.MapUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class ZoneSymbolizer implements Symbolizer<ZoneItem> {

	@Override
	public void symbolize(GC gc, ZoneItem item, MapWidgetContext context, int zoom) {
		paintGeometry(gc, item.getGeom(), item.isSelected(), context, zoom);
	}
	
	private void paintGeometry(GC gc, Geometry geom, boolean selected, MapWidgetContext context, int zoom) {

		Color lineColor = new Color (Display.getDefault(), 199, 22, 199);
		
		Point mapPosition = context.getMap().getMapPosition();
		
		if (geom instanceof LineString || geom instanceof MultiLineString) {
			Coordinate coords[] = geom.getCoordinates();

			int xpoint[] = new int[coords.length];
			int ypoint[] = new int[coords.length];

			for (int i = 0; i < coords.length; i++) {
				xpoint[i] = MapUtil.lon2position(coords[i].x, zoom)- mapPosition.x;
				ypoint[i] = MapUtil.lat2position(coords[i].y, zoom)- mapPosition.y;
			}

			int x0 = -1;
			int y0 = -1;
			for (int j = 0; j < xpoint.length; j++) {

				int x1 = xpoint[j];
				int y1 = ypoint[j];

				if (x0 != -1 && y0 != -1) {
					gc.setForeground(lineColor);
//					Stroke strokePrevio = g.getStroke();
//					g.setStroke(new BasicStroke(4.0f)); // 2-pixel lines
//					g.setColor(Color.DARK_GRAY);
//					g.drawLine(x1, y1, x0, y0);
//					g.setColor(new Color(255, 0, 128));
//					g.setStroke(new BasicStroke(3.0f)); // 2-pixel lines
//					g.drawLine(x1, y1, x0, y0);
//					g.setStroke(strokePrevio);
				}
				x0 = x1;
				y0 = y1;
			}

//			for (int j = 0; j < xpoint.length; j++) {
//				int x1 = xpoint[j];
//				int y1 = ypoint[j];
//				g.setColor(new Color(255, 0, 128));
//				g.fillArc(x1 - 4, y1 - 4, 8, 8, 0, 360);
//				g.setColor(Color.DARK_GRAY);
//				g.drawArc(x1 - 4, y1 - 4, 8, 8, 0, 360);
//			}
		}
			
		if (geom instanceof Polygon || geom instanceof MultiPolygon) {
			Coordinate coords[] = geom.getCoordinates();

			int points[] = new int[coords.length*2];

			for (int i = 0; i < coords.length; i++) {
				points[i*2] = MapUtil.lon2position(coords[i].x, zoom)- mapPosition.x;
				points[i*2+1] = MapUtil.lat2position(coords[i].y, zoom)- mapPosition.y;
			}

			gc.setAntialias(SWT.ON);
			gc.setForeground(lineColor);
			gc.setLineWidth(2);
			
			gc.drawPolygon(points);
			
			if (selected) {
				for (int j = 0; j < points.length; j+=2) {
					gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
					gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_RED));
					gc.setLineWidth(1);
				
					int x1 = points[j];
					int y1 = points[j+1];
					gc.fillArc(x1 - 3, y1 - 3, 6, 6, 0, 360);
					gc.drawArc(x1 - 3, y1 - 3, 6, 6, 0, 360);
				}
			}
		}
		
		lineColor.dispose();
	}

}
