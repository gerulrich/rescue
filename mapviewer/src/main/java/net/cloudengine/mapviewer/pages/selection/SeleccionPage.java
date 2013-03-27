
package net.cloudengine.mapviewer.pages.selection;
import net.cloudengine.app.MapApplication;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.pages.AbstractPage;
import net.cloudengine.mapviewer.pages.Page;
import net.cloudengine.mapviewer.pages.PageContainer;
import net.cloudengine.mapviewer.tools.selection.SelectionTool;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class SeleccionPage extends AbstractPage implements Page {
    
    private TreeViewer tree;
    private MapApplication app;
    
    private Image clearImage;
    private Image collapseImage;
    private Image expandImage;
    
    public SeleccionPage(MapApplication app) {
    	this.app = app;
    	clearImage = new Image(Display.getDefault(), getClass().getResourceAsStream("clear.png"));
    	collapseImage = new Image(Display.getDefault(), getClass().getResourceAsStream("collapse.png"));
    	expandImage = new Image(Display.getDefault(), getClass().getResourceAsStream("expand.png"));
    }
    
    protected void initContent(final PageContainer container, Composite composite) {
        addHeaderRow(container, composite, "Acciones");
        addActionLink(container, composite, "<a>Volver al menú principal</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(0);
            }
        });
        
        addHeaderRow(container, composite, StringUtils.EMPTY);
        
        Composite toolBar = createToolBar(container, composite, 5);
        addButton(toolBar, collapseImage, new SelectionAdapter() { 
        	public void widgetSelected(SelectionEvent e) {
				tree.collapseAll();
			}
        });
        addButton(toolBar, expandImage, new SelectionAdapter() { 
        	public void widgetSelected(SelectionEvent e) {
				tree.expandAll();
			}
        });     
        
        
        addButton(toolBar, clearImage, new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				app.getMapWidget().getTool(SelectionTool.class).clearSelection();
			} 
        });
        
        addHeaderRow(container, composite, "Selección");
        
    
        MapWidgetContext context = app.getMapWidget().getContext();
        
        tree = new TreeViewer(composite);
        tree.getTree().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2 , 1));

        tree.setContentProvider(new SelectionContentProvider(tree, context));
        tree.setLabelProvider(new SelectionLabelProvider());
        tree.setInput("root");
        tree.expandAll();
        

        
    }

    protected void widgetDisposed(DisposeEvent e) {
    	clearImage.dispose();
    	collapseImage.dispose();
    	expandImage.dispose();
    }

}
