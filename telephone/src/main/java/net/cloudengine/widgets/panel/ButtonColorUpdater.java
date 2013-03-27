package net.cloudengine.widgets.panel;

import net.cloudengine.widgets.sound.PhoneLine;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

public class ButtonColorUpdater extends java.util.TimerTask {
    public boolean odd = false;
    
    
    private PhoneLine lines[];
    private Button lineButtons[];
    
    public ButtonColorUpdater(PhoneLine[] lines, Button lineButtons[]) {
		super();
		this.lines = lines;
		this.lineButtons = lineButtons;
	}

	public void run() {
      int clr = Imagenes.PIC_BLACK;
      for(int a=0;a<6;a++) {
        if ((lines[a].sip != null) && (lines[a].auth)) {
          clr = Imagenes.PIC_GREY;
          if (lines[a].msgwaiting) {
            if (odd) clr = Imagenes.PIC_ORANGE;
          }
          if (lines[a].incoming) {
            if (odd) clr = Imagenes.PIC_GREEN; else clr = Imagenes.PIC_GREY;
          } else if (lines[a].incall) clr = Imagenes.PIC_GREEN;
        } else {
          if (lines[a].unauth)
            clr = Imagenes.PIC_RED;
          else
            clr = Imagenes.PIC_BLACK;
        }
        if (lines[a].clr != clr) {
            changeImage(lineButtons[a], Imagenes.getImage(clr));
            lines[a].clr = clr;
          }
        }
        odd = !odd;
      }
	
	private void changeImage(final Button b, final Image i) {
		if (Display.getDefault() == null) {
			return;
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!b.isDisposed()) {
					b.setImage(i);
				}
			}
		});
	}
  }