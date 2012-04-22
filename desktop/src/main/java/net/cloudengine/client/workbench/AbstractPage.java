package net.cloudengine.client.workbench;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractPage implements Page {

    private PageContainer container;
    private Composite composite;
    
    protected PageContainer getContainer() {
        return container;
    }
    
    protected Composite getComposite() {
        return composite;
    }
    
    public Control getControl(PageContainer container, Composite parent) {
        if (composite == null) {
            this.container = container;
            composite = new Composite(parent, SWT.NONE);
            composite.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                   AbstractPage.this.widgetDisposed(e);
                }
            });
            
            composite.setLayout(new GridLayout(2, false));
            container.adapt(composite);
            
            
            initContent(container, composite);
        }
        return composite;
    }
    
    protected abstract void widgetDisposed(DisposeEvent e);
    
    protected abstract void initContent(PageContainer container, Composite composite);



}


