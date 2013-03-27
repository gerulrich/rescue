package net.cloudengine.widgets.panel;

import net.cloudengine.util.GuiUtil;
import net.cloudengine.widgets.sound.MeterController;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;

public class MeterControl extends Composite implements MeterController {

	private Image recImg;
	private Image playImg;
	
	private ProgressBar meter;
	private Scale scale;
	private boolean recording;
	private Button button;
	
	public MeterControl(Composite parent, int style, boolean recording) {
		super(parent, style);
		this.recording = recording;
		try {
			recImg = GuiUtil.getImage("rec.png");
			playImg = GuiUtil.getImage("play.png");
		} catch (Exception e) {
			e.printStackTrace();
		}		
		createGui(style);
	}
	
	private void createGui(int style) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		this.setLayout(gridLayout);		
		
		scale = new Scale(this, SWT.VERTICAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.widthHint = 32;
		gridData.heightHint = 85;
		scale.setLayoutData(gridData);
		
		meter = new ProgressBar(this, SWT.VERTICAL | SWT.SMOOTH);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.widthHint = 5;
		gridData.heightHint = 70;
		meter.setLayoutData(gridData);
		
		button = new Button(this, SWT.NONE);
		button.setImage(Imagenes.getImage(Imagenes.PIC_GREY));
//		if (recording) {
//			button.setText("MUT");
//		} else {
//			button.setText("SPK");
//		}
		
		Label l = new Label(this, SWT.NONE);
		if (recording) {
			l.setImage(recImg);
		} else {
			l.setImage(playImg);
		}
	}

	@Override
	public void setSpeakerStatus(boolean state) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setMeterRec(final int lvl) {
		if (!recording || this.isDisposed())
			return;
		this.getDisplay().asyncExec(new Runnable() {
            public void run() {
              if (meter.isDisposed())
                return;
              meter.setSelection(lvl);
            }
		});		
	}

	@Override
	public void setMeterPlay(final int lvl) {
		if (recording || this.isDisposed())
			return;
		this.getDisplay().asyncExec(new Runnable() {
            public void run() {
              if (meter.isDisposed())
                return;
              meter.setSelection(lvl);
            }
		});
	}

	public void setImageButton(Image image) {
		GuiUtil.changeImage(button, image);
	}

	@Override
	public void dispose() {
		recImg.dispose();
		playImg.dispose();
		super.dispose();
	}
	
	

}
