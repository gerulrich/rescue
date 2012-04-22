package net.cloudengine.app.actions;

import net.cloudengine.app.Application;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class SelectAction extends Action {
	
//	  private Application app;
	  /**
	   * AboutAction constructor
	   */
	  public SelectAction(Application app) {
	    super("Selección", SWT.PUSH);
	    this.setImageDescriptor(ImageDescriptor.createFromFile(SelectAction.class, "select.png"));
//	    setDisabledImageDescriptor(ImageDescriptor.createFromFile(AboutAction.class, "/images/disabledAbout.gif"));
	    setToolTipText("Selección de objetos del mapa");
//	    this.app = app;
	  }


}
