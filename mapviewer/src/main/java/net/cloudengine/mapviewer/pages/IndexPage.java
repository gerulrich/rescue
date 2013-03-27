package net.cloudengine.mapviewer.pages;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

public class IndexPage extends AbstractPage implements Page {

	protected void initContent(final PageContainer container, Composite composite) {
		addHeaderRow(container, composite, "Acciones");
        addActionLink(container, composite, "<a>Busqueda</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(1);
            }
        });
        addActionLink(container, composite, "<a>Información Técnica</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(2);
            }
        });
        addActionLink(container, composite, "<a>Selección</a>", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                container.showPage(3);
            }
        });        
        
        addHeaderRow(container, composite, "Ayuda");
        addInfoText(container, composite, 
        		"Utilice la rueda del ratón para acercar y alejar la imagen. " +
        		"Arrastre el ratón para cambiar su posición en el mapa");
	}

	protected void widgetDisposed(DisposeEvent e) {
		
	}


}
