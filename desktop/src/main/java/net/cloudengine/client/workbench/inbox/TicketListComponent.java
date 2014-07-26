package net.cloudengine.client.workbench.inbox;

import net.cloudengine.rpc.model.TicketViewModel;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TicketListComponent extends Composite {

	private TableViewer tableViewer;
	
	public TicketListComponent(Composite parent, int style, FolderSource<TicketViewModel> folderSource) {
		super(parent, style);
		this.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(this, SWT.FULL_SELECTION);
		
		// Set the content and label providers
		tableViewer.setContentProvider(new InboxTicketContentProvider(tableViewer, folderSource));
		tableViewer.setLabelProvider(new InboxTicketLabelProvider());
		
		Table table = tableViewer.getTable();
	    table.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
	    TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth(80);
		column.setText("ID");
		
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(120);
		column.setText("Fecha");
		
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(160);
		column.setText("Usuario asignado");
		
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(120);
		column.setText("Estado");
		
		column = new TableColumn(table, SWT.NONE);
		column.setWidth(140);
		column.setText("Creador");
		
		tableViewer.setInput("root");
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);		
	}
}
