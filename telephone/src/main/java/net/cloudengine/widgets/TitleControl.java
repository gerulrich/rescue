/*;
 * (c) 2008, stepan rutz, stepan.rutz@gmx.de 
 */
package net.cloudengine.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * @author stepan.rutz
 * @version $Revision$
 */
public class TitleControl extends Canvas {

	private static final int TOP_SPACE = 0;
	private Font font;
	private Color gradient1Color;
	private Color gradient2Color;
	private Color bottomLineColor;
	private Color writingColor;
//	private Image image;
	private String text = "";
	private Point size = new Point(1, 1);

	public TitleControl(Composite parent) {
		super(parent, SWT.DOUBLE_BUFFERED);
		addPaintListener(new PaintListener() {
			
			public void paintControl(PaintEvent e) {
				onPaint(e);
			}
		});
		addDisposeListener(new DisposeListener() {
			
			public void widgetDisposed(DisposeEvent e) {
				onDispose(e);
			}
		});

		font = new Font(getDisplay(), "Tahoma", 10, SWT.BOLD);
		gradient1Color = new Color(getDisplay(), 255, 255, 255);
		gradient2Color = new Color(getDisplay(), 205, 224, 244);
		bottomLineColor = new Color(getDisplay(), 200, 195, 216);
		writingColor = new Color(getDisplay(), 60, 60, 60);
//		image = new Image(getDisplay(), getClass().getResourceAsStream("world.png"));
		measureSize("M");
	}

	private void measureSize(String s) {
		GC gc = new GC(this);
		try {
			gc.setFont(font);
			size = gc.stringExtent(s);
//			size.y = Math.max(image.getBounds().height + 1, size.y);
			size.y += TOP_SPACE;
		} finally {
			gc.dispose();
		}
	}

	private void onDispose(DisposeEvent e) {
		font.dispose();
		gradient1Color.dispose();
		gradient2Color.dispose();
		bottomLineColor.dispose();
		writingColor.dispose();
//		image.dispose();
	}

	private void onPaint(PaintEvent e) {
		GC gc = e.gc;
		Point s = getSize();
		int w = s.x;
		int h = s.y;

		gc.setForeground(gradient1Color);
		gc.setBackground(gradient2Color);
		gc.fillGradientRectangle(0, 0, w, h - 1, true);

//		Rectangle imgsize = image.getBounds();
//		gc.drawImage(image, 12, 0);
		gc.setForeground(bottomLineColor);
		gc.drawLine(0, h - 1, w, h - 1);

		gc.setFont(font);
		gc.setForeground(writingColor);
		Point textSize = gc.stringExtent(text);
		int ty = (h - textSize.y) / 2;
		gc.drawString(text, 22 , TOP_SPACE + ty, true);
		gc.drawRectangle(0, 0, w-1, h);
//		gc.drawString(text, 22 + imgsize.width, TOP_SPACE + ty, true);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		measureSize(text);
		redraw();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(size.x, size.y);
	}

}
