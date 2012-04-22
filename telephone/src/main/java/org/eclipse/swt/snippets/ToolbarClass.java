package org.eclipse.swt.snippets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolbarClass {

  public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setSize(300, 200);
    shell.setText("Toolbar Example");

    ToolBar toolbar = new ToolBar(shell, SWT.NONE);
    toolbar.setBounds(0, 0, 200, 70);

    ToolItem toolItem1 = new ToolItem(toolbar, SWT.FLAT);
    toolItem1.setText("Save");
    ToolItem toolItem2 = new ToolItem(toolbar, SWT.PUSH);
    toolItem2.setText("Save As");
    ToolItem toolItem3 = new ToolItem(toolbar, SWT.PUSH);
    toolItem3.setText("Print");
    ToolItem toolItem4 = new ToolItem(toolbar, SWT.PUSH);
    toolItem4.setText("Run");
    ToolItem toolItem5 = new ToolItem(toolbar, SWT.PUSH);
    toolItem5.setText("Help");

    final Text text = new Text(shell, SWT.BORDER);
    text.setBounds(55, 80, 200, 25);

    Listener toolbarListener = new Listener() {
      public void handleEvent(Event event) {
        ToolItem toolItem = (ToolItem) event.widget;
        String caption = toolItem.getText();
        text.setText("You clicked " + caption);
      }
    };

    toolItem1.addListener(SWT.Selection, toolbarListener);
    toolItem2.addListener(SWT.Selection, toolbarListener);
    toolItem3.addListener(SWT.Selection, toolbarListener);
    toolItem4.addListener(SWT.Selection, toolbarListener);
    toolItem5.addListener(SWT.Selection, toolbarListener);

    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}