package net.cloudengine.client.main;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class SWTGraphicsUtils {

	/**
	 * Centra un shell en la pantalla.
	 * 
	 * @param shell
	 *            shell a centrar.
	 */
	public static void centerShell(Shell shell) {
		final Monitor primary = shell.getDisplay().getPrimaryMonitor();
		final Rectangle bounds = primary.getBounds();
		final Rectangle rect = shell.getBounds();
		final int x = bounds.x + (bounds.width - rect.width) / 2;
		final int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	/**
	 * Build a font from a given control. Useful if we just want a bold label
	 * for example
	 * 
	 * @param control
	 *            control that handle the default font
	 * @param style
	 *            new style
	 * @return a font with the given style
	 */
	public static Font buildFontFrom(final Control control, final int style) {
		final Font temp = control.getFont();
		final FontData[] fontData = temp.getFontData();
		if (fontData == null || fontData.length == 0) {
			return temp;
		}
		return new Font(control.getDisplay(), fontData[0].getName(),
				fontData[0].getHeight(), style);
	}

	/**
	 * Dispose safely any SWT resource
	 * 
	 * @param r the resource to dispose
	 */
	public static void dispose(final Resource r) {
		if (r != null && !r.isDisposed()) {
			r.dispose();
		}
	}

}
