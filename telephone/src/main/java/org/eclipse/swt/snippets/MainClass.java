package org.eclipse.swt.snippets;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainClass extends ApplicationWindow {
	public MainClass() {
		super(null);
		addCoolBar(SWT.NONE);
	}

	@Override
	protected CoolBarManager createCoolBarManager(int style) {

		CoolBarManager cbm = new CoolBarManager();
		ToolBarManager tbm = new ToolBarManager();
		tbm.add(new Action() {
			{
				setText("Hola");
			}
		});
		cbm.add(new Item1("123213"));
		cbm.add(tbm);
		return cbm;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Monitor de ext. telefonicas");
		shell.setSize(400, 400);
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		// Button preserveCase = new Button(composite, SWT.CHECK);
		// preserveCase.setText("&Preserve case");
		// final TreeViewer tv = new TreeViewer(composite);
		// tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		// tv.setContentProvider(new FileTreeContentProvider());
		// tv.setLabelProvider(new FileTreeLabelProvider());
		// tv.setInput("root");

		return composite;
	}

	public static void main(String[] args) {
		MainClass app = new MainClass();
		app.setBlockOnOpen(true);
		app.open();
		Display.getCurrent().dispose();
	}
}
