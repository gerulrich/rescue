package net.cloudengine.client.main;

import java.util.ArrayList;
import java.util.Collection;

import net.cloudengine.client.ui.AnnotatedCallbackResolver;
import net.cloudengine.client.ui.JobUtils;
import net.cloudengine.client.ui.PostCallback;
import net.cloudengine.rpc.controller.config.PropertyController;
import net.cloudengine.rpc.controller.config.PropertyModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class PropertiesWindow extends ApplicationWindow {

	private PropertyController controller;
    final private Collection<PropertyModel> properties = new ArrayList<PropertyModel>();
    private TableViewer tv;

	public PropertiesWindow(Shell parentShell, PropertyController controller) {
		super(parentShell);
		this.controller = controller;
		this.addStatusLine();
	}

	/**
	 * Configures the shell
	 * 
	 * @param shell the shell
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		this.setBlockOnOpen(true);
		shell.setSize(400, 400);
		shell.setText("Propiedades de configuración");
		SWTGraphicsUtils.centerShell(shell);
	}

	/**
	 * Creates the main window's contents
	 * 
	 * @param parent the main window
	 * @return Control
	 */
	protected Control createContents(Composite parent) {
		// Create the composite to hold the controls
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		// Create the combo to hold the team names

		// Create the table viewer to display the players
		tv = new TableViewer(composite);

		// Set the content and label providers
		tv.setContentProvider(new MyContentProvider(properties));
		tv.setLabelProvider(new MyLabelProvider());

		// Set up the table
		Table table = tv.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Add the first name column
		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setWidth(150);
		column.setText("Nombre");

		// Add the last name column
		column = new TableColumn(table, SWT.LEFT);
		column.setWidth(150);
		column.setText("Valor");

		// Pack the columns
//		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
//			table.getColumn(i).pack();
//		}

		// Turn on the header and the lines
		tv.setInput("root");
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		IProgressMonitor monitor = this.getStatusLineManager().getProgressMonitor();
		JobUtils.execAsync(controller, new AnnotatedCallbackResolver(this, "update"), monitor).getProperties();

		return composite;
	}
	
	@PostCallback(name="update")
	public void updateProperties(Collection<PropertyModel> newProperties) {
		properties.clear();
		properties.addAll(newProperties);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tv.refresh();
			}
		});
	}	
	
}


/**
 * This class provides the labels for the person table
 */

class MyLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		PropertyModel prop = (PropertyModel) element;
	    switch (columnIndex) {
	    case 0:
	      return prop.getName();
	    case 1:
	      return prop.getValue();
	    }
	    return null;
	}

	/**
	 * Adds a listener
	 * 
	 * @param listener the listener
	 */
	public void addListener(ILabelProviderListener listener) {
		// Ignore it
	}

	/**
	 * Disposes any created resources
	 */
	public void dispose() {
		// Nothing to dispose
	}

	/**
	 * Returns whether altering this property on this element will affect the
	 * label
	 * 
	 * @param element the element
	 * @param property the property
	 * @return boolean
	 */
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	/**
	 * Removes a listener
	 * 
	 * @param listener the listener
	 */
	public void removeListener(ILabelProviderListener listener) {
		// Ignore
	}
}

class MyContentProvider implements IStructuredContentProvider {

	private Collection<PropertyModel> properties;
	
	public MyContentProvider(Collection<PropertyModel> properties) {
		super();
		this.properties = properties;
	}

	public Object[] getElements(Object arg0) {
		return properties.toArray();
	}

	public void dispose() {
		// We don't create any resources, so we don't dispose any
	}

	/**
	 * Called when the input changes
	 * 
	 * @param arg0 the parent viewer
	 * @param arg1 the old input
	 * @param arg2 the new input
	 */
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// Nothing to do
	}
}