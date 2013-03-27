package net.cloudengine.widgets.panel;

import net.cloudengine.util.GuiUtil;

import org.eclipse.swt.graphics.Image;

public class Imagenes {

	public static int PIC_BLACK = 0;
	public static int PIC_GREEN = 1;
	public static int PIC_RED = 2;
	public static int PIC_GREY = 3;
	public static int PIC_ORANGE = 4;
	public static int PIC_MIC = 5;
	public static int PIC_HEADSET = 6;
	public static int PIC_MUTE = 7;
	public static int PIC_SPK = 8;
	public static int PIC_SWSCALE = 9;
	public static final int PIC_HWSCALE = 10;
	public static final int PIC_ICON_OPEN = 11;
	public static final int PIC_ICON_CLOSED = 12;
	public static final int PIC_ICON_BUSY = 13;
	public static final int PIC_ICON_IDLE = 14;
	public static final int PIC_ICON_DND = 15;
	public static final int PIC_LABELS1 = 16;
	public static final int PIC_LABELS2 = 17;
	public static final int PIC_TRAY_24 = 18;
	public static final int PIC_TRAY_16 = 19;
	public static final int PIC_CALL = 20;
	public static final int PIC_END = 21;
	public static final int PIC_CALL2 = 22;
	public static final int PIC_END2 = 23;
	public static final int PIC_LOGO = 24;
	public static final int PIC_VIDEO = 25;

	public static String icons[] = { "blk.png", "grn.png", "red.png",
			"grey.png", "orange.png", "mic.png", "headset.png", "mute.png",
			"spk.png", "swscale.png", "hwscale.png", "icon_open.png",
			"icon_closed.png", "icon_busy.png", "icon_idle.png",
			"icon_dnd.png", "labels1.png", "labels2.png", "icon-24x24.png",
			"icon-16x16.png", "call.png", "end.png", "call2.png", "end2.png",
			"logo.png", "video.png" };

	public static Image ii[];

	static {
		ii = new Image[icons.length];
		for (int a = 0; a < icons.length; a++) {
			try {
				ii[a] = GuiUtil.getImage(icons[a]);
			} catch (Exception e) {
				// TODO log
				e.printStackTrace();
			}
		}
	}
	
	public static Image getImage(int value) {
		if (value < 0 || value > 25)
			throw new IllegalArgumentException("Numero de imagen inválido");
		return ii[value];
	}

}
