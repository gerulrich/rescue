package org.eclipse.swt.snippets;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class App extends ApplicationWindow {

  public App(Shell parent) {
    super(parent);
  }

  @Override
  protected Control createContents(Composite parent) {
    getShell().setText("CoolBarManager example");

    return super.createContents(parent);
  }

  @Override
  public void create() {
    addCoolBar(SWT.FLAT);
    super.create();
  }

  @Override
  protected CoolBarManager createCoolBarManager(int style) {
    CoolBarManager cbm = new CoolBarManager(style);

    IToolBarManager tb1 = new ToolBarManager(style);
    IToolBarManager tb2 = new ToolBarManager(style);

    tb1.add(new Action() {
      {
        setText("&Button1");
      }
    });
    tb1.add(new Action() {
      {
        setText("&Button2");
      }
    });
    tb1.add(new Action() {
      {
        setText("&Button3");
      }
    });

    tb2.add(new Action() {
      {
        setText("&Button4");
      }
    });

    tb2.add(new Action() {
      {
        setText("&Button5");
      }
    });

    cbm.add(tb1);
    cbm.add(tb2);

    return cbm;
  }

  public static void main(String[] args) {
    App app = new App(null);

    app.setBlockOnOpen(true);
    app.open();

    Display.getCurrent().dispose();
  }
}