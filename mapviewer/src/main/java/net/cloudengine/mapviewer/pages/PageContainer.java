package net.cloudengine.mapviewer.pages;
import java.util.ArrayList;
import java.util.Arrays;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class PageContainer extends Composite {

    private Composite content;
    private StackLayout stackLayout;
    private ArrayList<Page> pages = new ArrayList<Page>();
    private int activePageIndex = -1;

    public PageContainer(Composite parent, int style) {
        super(parent, style);
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
               PageContainer.this.widgetDisposed(e);
            }
        });
        adapt(this);
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = layout.marginHeight = 0;
        setLayout(layout);
        
        stackLayout = new StackLayout();

        content = new Composite(this, SWT.NONE);
        content.setLayout(stackLayout);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        adapt(content);
    }
    
    protected void widgetDisposed(DisposeEvent e) {
    }

    public void setPages(Page... pages) {
        this.pages.clear();
        this.pages.addAll(Arrays.asList(pages));
    }
    
    public Page[] getPages() {
        return pages.toArray(new Page[pages.size()]);
    }
    
    public int indexOfPage(Page page) {
        return pages.indexOf(page);
    }
    
    public int getActivePageIndex() {
        return activePageIndex;
    }
    
    public void showPage(int index) {
        stackLayout.topControl = pages.get(index).getControl(this, content);
        content.layout();
        activePageIndex = index;
    }
    
    public void adapt(Control control) {
        control.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
    }
}


