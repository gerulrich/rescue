package net.cloudengine.widgets.sound;


public class SoundUtil {
	
	private short silence[] = new short[160];
	private final double ringVol = 8000.0;
	private int ring_440, ring_480;
	private int ringCycle;
	private int ringCount;
	private int wait_440;
	private int waitCycle;
	
	public short[] getSilence() {
		return silence;
	}
	
	/** Retorna los proximos 20ms de sonido para el 
	 * ringigng del telefono.
	 */
	public void startRinging() {
		ring_440 = 0;
		ring_480 = 0;
		ringCycle = 0;
		ringCount = 0;
		wait_440 = 0;
		waitCycle = 0;
	}
	
//	/** Returns a list of sound mixers in the system. */
//	public static String[] getMixers() {
//		ArrayList<String> mixers = new ArrayList<String>();
//		Mixer.Info mi[] = AudioSystem.getMixerInfo();
//		mixers.add("<default>");
//		for(int a=0;a<mi.length;a++) {
//			mixers.add(mi[a].getName());
//		}
//		String sa[] = new String[mixers.size()];
//		for(int a=0;a<mixers.size();a++) {
//			sa[a] = mixers.get(a);
//		}
//		return sa;
//	}
	
	
	/** Scales samples to a software volume control. */
	public static void scaleBuffer(short buf[], int start, int len, int scale) {
		float fscale;
		if (scale == 0) {
			for (int a = 0; a < 160; a++) {
				buf[a] = 0;
			}
		} else {
			if (scale <= 75) {
				fscale = 1.0f - ((75-scale) * 0.014f);
				for (int a = 0; a < 160; a++) {
					buf[a] = (short) (buf[a] * fscale);
				}
			} else {
				fscale = 1.0f + ((scale-75) * 0.04f);
				float value;
				for (int a = 0; a < 160; a++) {
					value = buf[a] * fscale;
					if (value < Short.MIN_VALUE) buf[a] = Short.MIN_VALUE;
					else if (value > Short.MAX_VALUE) buf[a] = Short.MAX_VALUE;
					else buf[a] = (short)value;
				}
			}
		}
	}
	
	//big endian
	public static byte[] short2byte(short buf[], boolean resample441k) {
		byte buf8[];
		if (resample441k) {
			buf8 = new byte[882 * 2];
			int pos = 0;
			float x1 = 0.5125f;
			float x2 = 0.0f;
			int x;
			for (int a = 0; a < 160; a++) {
				x2 += x1;
				x = 5 + (int) x2;
				if (x2 >= 1.0f) x2 -= 1.0f;
				//TODO : interpolate data
				for (int b = 0; b < x; b++) {
					buf8[pos * 2] = (byte) (buf[a] >>> 8);
					buf8[pos * 2 + 1] = (byte) (buf[a] & 0xff);
					pos++;
					if (pos > 881) pos = 881;
				}
			}
		} else {
			buf8 = new byte[160 * 2];
			for (int a = 0; a < 160; a++) {
				buf8[a * 2] = (byte) (buf[a] >>> 8);
				buf8[a * 2 + 1] = (byte) (buf[a] & 0xff);
			}
		}
		return buf8;
	}
	
	//big endian
	public static void byte2short(byte buf8[], short buf16[], boolean resample441k) {
		if (resample441k) {
			int pos = 0;
			float x1 = 0.5125f;
			float x2 = 0.0f;
			int x;
			for (int a = 0; a < 160; a++) {
				x2 += x1;
				x = 5 + (int) x2;
				if (x2 >= 1.0)
					x2 -= 1.0f;
				buf16[a] = (short) ((((short) (buf8[pos * 2])) << 8) + (((short) (buf8[pos * 2 + 1])) & 0xff));
				pos += x;
				if (pos > 881)
					pos = 881;
			}
		} else {
			for (int a = 0; a < 160; a++) {
				buf16[a] = (short) ((((short) (buf8[a * 2])) << 8) + (((short) (buf8[a * 2 + 1])) & 0xff));
			}
		}
	}
	
	  
	public short[] getRinging() {
		//440 + 480
		//2 seconds on/3 seconds off
		  
//		if ((inRinging) && (wav.isLoaded())) {
//		  return wav.getSamples();
//		}
		  
		ringCount += 160;
		if (ringCount == 8000) {
			ringCount = 0;
			ringCycle++;
		}
		if (ringCycle == 5) ringCycle = 0;
		if (ringCycle > 1) {
			ring_440 = 0;
			ring_480 = 0;
			return silence;
		}
		short buf[] = new short[160];
	    //440
		for (int a = 0; a < 160; a++) {
			buf[a] = (short) (Math.sin((2.0 * Math.PI / (8000.0 / 440.0)) * (a + ring_440)) * ringVol);
		}
		ring_440 += 160;
		if (ring_440 == 8000) ring_440 = 0;
		//480
		for (int a = 0; a < 160; a++) {
			buf[a] += (short) (Math.sin((2.0 * Math.PI / (8000.0 / 480.0)) * (a + ring_480)) * ringVol);
		}
		ring_480 += 160;
		if (ring_480 == 8000) ring_480 = 0;
		return buf;
	}
	
	/** Returns next 20ms of a generated call waiting sound (beep beep). */
	public short[] getCallWaiting() {
		//440 (2 bursts for 0.3 seconds)
		//2on 2off 2on 200off[4sec]
		waitCycle++;
		if (waitCycle == 206) waitCycle = 0;
		if ((waitCycle > 6) || (waitCycle == 2) || (waitCycle == 3)) {
			wait_440 = 0;
			return silence;
		}
		short buf[] = new short[160];
		//440
		for (int a = 0; a < 160; a++) {
			buf[a] = (short) (Math.sin((2.0 * Math.PI / (8000.0 / 440.0)) * (a + wait_440)) * ringVol);
		}
		wait_440 += 160;
		if (wait_440 == 8000) wait_440 = 0;
		return buf;
	}

}
