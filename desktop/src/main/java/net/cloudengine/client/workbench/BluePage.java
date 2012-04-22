package net.cloudengine.client.workbench;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class BluePage extends AbstractPage {
	
	@Override
	protected void initContent(PageContainer container, Composite composite) {
		composite.setLayout(new FillLayout());
		final Composite comp = new Composite(composite, SWT.NONE);
		comp.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
				e.gc.fillRectangle(comp.getBounds());
				e.gc.drawString("Perspectiva de usuario 3 (azul)", 100, 100);
			}
		});
	}	
	
	@Override
	protected void widgetDisposed(DisposeEvent e) {

	}



}
