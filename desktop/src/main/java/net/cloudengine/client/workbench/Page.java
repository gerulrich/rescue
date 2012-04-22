package net.cloudengine.client.workbench;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


public interface Page {
	
    Control getControl(PageContainer container, Composite parent);
}