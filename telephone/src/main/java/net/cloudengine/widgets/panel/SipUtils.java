package net.cloudengine.widgets.panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import javaforce.JFLog;
import javaforce.voip.Codec;
import javaforce.voip.RTP;
import javaforce.voip.RTPInterface;
import javaforce.voip.SIP;
import javaforce.voip.SIPClient;
import net.cloudengine.widgets.sound.PhoneLine;
import net.cloudengine.widgets.sound.Settings;
import net.cloudengine.widgets.sound.Sound;

public class SipUtils {
	
	private static final String DIALING_STATUS = "Dialing";
	private static final String CONNECTED_STATUS = "Connected";
	private static final String XFER_MESSAGE = "Enter dest and then XFER again";
	private static final String XFER_TO = "XFER to ";
	private static final String EMPTY = "";
	
	private static final int LINES_COUNT = 6;
	
	/** 
	 * Empieza una nueva llamada o acepta una llamada entrante.  
	 */	
	public static void call(PhoneLine line, Sound sound, GUI gui, RTPInterface rtpInterface) {
		gui.updateCallButton(false);
		if (!isValid(line)) {
			return;
		}
		
		if (line.sip == null) 
			return;
		if (!line.auth) 
			return;
		if (line.incall) {
			gui.updateCallButton(true); 
			return;
		}  //already in call
		if (line.dial.length() == 0) {
			gui.updateCallButton(false); 
			return;
		}
		gui.updateCallButton(true);
		if (line.incoming) {
			callAccept(line, sound, rtpInterface);
		} else {
			callInvite(line, gui, rtpInterface);
		}
//		if (Settings.current.ac) {
//			if (!lines[line].cnf) doConference();
//		}
		gui.updateLine();
	}
	
	/**
	 * Incia una llamada.
	 * @param line
	 * @param rtpInterface
	 */
	public static void callInvite(PhoneLine line, GUI gui, RTPInterface rtpInterface) {
	    line.rtp = new RTP();
	    line.rtp.init(rtpInterface);
	    line.Vrtp = new RTP();
	    line.Vrtp.init(rtpInterface);
	    line.Vrtp.setMTU(32768);
	    line.incall = true;
	    line.trying = false;
	    line.ringing = false;
	    line.talking = false;
	    line.incoming = false;
	    line.status = DIALING_STATUS;

	    Codec[] codecs = new Codec[] {
	    		RTP.CODEC_G729a, 
	    		RTP.CODEC_G711u
	    };

	    line.callid = line.sip.invite(line.dial, line.rtp.getlocalrtpport(), line.Vrtp.getlocalrtpport(), codecs);
	    gui.callInviteUpdate();
	}
	
	public static void onInviteSuccess(PhoneLine line, SIPClient client, String callid, String remotertphost, 
			int remotertpport, int remoteVrtpport, Codec[] codecs) {
		
	}
	
	/**
	 * Acepta una llamada.
	 * @param line
	 * @param rtpInterface
	 */
	public static void callAccept(PhoneLine line, Sound sound, RTPInterface rtpInterface) {
		if (!SIP.hasCodec(line.codecs, RTP.CODEC_G729a) && !SIP.hasCodec(line.codecs, RTP.CODEC_G711u)) {
			// TODO 
			JFLog.log("callAccept() Failed : No compatible audio codec offered");
			
			line.sip.deny(line.callid, "NEED g711u or g729a", 415);
//			onCancel(line.sip, line.callid, 415);
			return;
		}
		    
		line.to = line.dial;
		line.rtp = new RTP();
		line.rtp.init(rtpInterface);
		    
		if (line.remoteVrtpport != -1) {
			line.Vrtp = new RTP();
			line.Vrtp.init(rtpInterface);
			line.Vrtp.setMTU(32768);
		}
		    
		if (SIP.hasCodec(line.codecs, RTP.CODEC_G729a) &&
				SIP.hasCodec(line.codecs, RTP.CODEC_G711u)) {
			line.codecs = SIP.delCodec(line.codecs, RTP.CODEC_G711u);
		}

		line.sip.accept(line.callid, line.rtp.getlocalrtpport(),
		      (line.remoteVrtpport != -1 ? line.Vrtp.getlocalrtpport() : -1), line.codecs);
		sound.flush();

		line.rtp.start(line.remotertphost, line.remotertpport, line.codecs, false);
		if (line.remoteVrtpport != -1) {
			line.Vrtp.start(line.remotertphost, line.remoteVrtpport, line.codecs, true);
		}

		line.incall = true;
		line.ringing = false;
		line.incoming = false;
		line.talking = true;
		line.status = CONNECTED_STATUS;
	}
	
	private static boolean isValid(PhoneLine line) {
		return line != null;
	}
	
	
	public static void end(PhoneLine line, GUI gui) {
		gui.updateEndButton(false);
		
		if (!isValid(line)) {
			return;
		}
		
		if (line.incoming) {
			line.sip.deny(line.callid, "IGNORE", 480);
			line.incoming = false;
			line.ringing = false;
			line.dial = "";
			line.status = "Hungup";
			gui.updateLine();
			return;
		}
		line.dial = "";
		if (!line.incall) {
			//no call (update status)
			if ((line.sip != null) && (!line.unauth)) 
				line.status = Estados.READY+" (" + line.user + ")";
			gui.updateLine();
			return;
		}
		if (line.talking)
			line.sip.bye(line.callid);
		else
			line.sip.cancel(line.callid);
		endLine(line, gui);
	}
	
	/** Cleanup after a call is terminated (call terminated local or remote). */
	private static void endLine(PhoneLine line, GUI gui) {
		gui.updateEndButton(false);
		gui.updateCallButton(false);
		
		line.dial = "";
		line.status = Estados.HUNGUP;
		line.trying = false;
		line.ringing = false;
		line.incoming = false;
		line.hld = false;
		line.cnf = false;
		line.xfer = false;
		line.incall = false;
		line.talking = false;
		line.rtp.stop();
		line.rtp = null;
		line.Vrtp = null;
		line.callid = "";

		gui.updateLine();
		gui.endLineUpdate(line);
	}	
	
	
	
	
	/**
	 * Busca la linea que tiene una llamada en curso
	 * con el callId especificado,
	 * @param lines
	 * @param callId
	 * @return
	 */
	public static PhoneLine findByCallId(PhoneLine lines[], String callid) {
		PhoneLine line = null;
		for(int a = 0; a < LINES_COUNT; a++ ) {
			if (lines[a].incall) {
				if (lines[a].callid.equals(callid)) {
					line = lines[a];
					break;
				}
			}
		}
		return line;
	}
	
	
	// ~~ Manejo de digitos del teclado. ==================================================
	
	/** Agrega un digito para ser marcado */
	public static void addDigit(PhoneLine line, GUI gui, char digit) {
		// valido que la linea sea válida.
		if (!isValid(line) || line.sip == null) {
			return;
		}
		
		if (!line.auth) return;
		if (line.incoming) return;
	    
		if (digit == SWT.BS) { // BACKSPACE
			if ((line.incall) && (!line.xfer)) 
				return;

		  // Elimino el ultimo digito
	      int len = line.dial.length();
	      if (len > 0) line.dial = line.dial.substring(0, len-1);
	    } else {
	    	if ((line.incall) && (!line.xfer)) 
	    	  return;
	        // Agrego el digito marcado
	    	line.dial += digit;
	    }
	    gui.updateLine();
	}
	
	public static void pressDigit(PhoneLine line, char digit) {
	    if (!isValid(line)) {
	    	return;
	    }
	    if (line.xfer) return;
	    line.dtmf = digit;
	}
	
	public static void releaseDigit(PhoneLine line, char digit) {
		if (!isValid(line)) {
	    	return;
	    }
	    if (line.dtmf != 'x') { 
	    	line.dtmfend = true;
	    }
	}
	
	
	// ~~ Métodos para las acciones =================================
	
	/**
	 * Comienza y finaliza una transferencia de llamada.
	 * @param line
	 * @param gui
	 */
	public static void doXfer(PhoneLine line, GUI gui) {
		if (!isValid(line) || !line.incall) {
			return;
		}
		
		if (line.xfer) {
			if (line.dial.length() == 0) {
				//cancelar xfer
				line.status = CONNECTED_STATUS;
	        	line.xfer = false;
			} else {
				line.sip.refer(line.callid, line.dial);
				line.status = XFER_TO + line.dial;
				line.dial = EMPTY;
				line.xfer = false;
				endLine(line, gui);
	      }
	    } else {
	    	line.dial = EMPTY;
	    	line.status = XFER_MESSAGE;
	    	line.xfer = true;
	    }
	    gui.updateLine();
	}
	
	/**
	 * Pone en hold la llamada.
	 * @param line
	 * @param gui
	 */
	public static void doHold(PhoneLine line, GUI gui) {
		if (!isValid(line) || !line.incall) {
			return;
		}
		
		Image image = Imagenes.getImage(Imagenes.PIC_GREEN);
		gui.hld_setIcon(image);
		
//		if (!isValid(line) || !line.incall) {
//			return;
//		}
		
		//no se puede poner en hold la llamada si ya estoy en hold del otro lado
		if (line.sip.isHold(line.callid)) {
			return;  
		}
		if (line.hld) {
			line.sip.reinvite(line.callid, line.rtp.getlocalrtpport(), line.codecs);
			line.hld = false;
		} else {
			line.sip.hold(line.callid, line.rtp.getlocalrtpport());
			line.hld = true;
		}

		image = Imagenes.getImage(line.hld ? Imagenes.PIC_RED : Imagenes.PIC_GREY);
		gui.hld_setIcon(image);
	}
	
	
	public static void toggleAA(Settings settings, GUI gui) {
		settings.setAa(!settings.isAa());
	    gui.aa_setIcon(Imagenes.getImage(settings.isAa() ? Imagenes.PIC_GREEN : Imagenes.PIC_GREY));
	}
	
	public static void toggleDND(PhoneLine line, Sound sound, GUI gui, RTPInterface rtpInterface) {
		if (!isValid(line) || line.incall) {
			return;
		}
		
		line.dnd = !line.dnd;
		Image image = Imagenes.getImage(line.dnd ? Imagenes.PIC_RED : Imagenes.PIC_GREY);
		gui.dnd_setIcon(image);

		if (line.dnd)
			line.dial = "*78";
		else
			line.dial = "*79";
		call(line, sound, gui, rtpInterface);
	}
	
	public static void doMonitor(PhoneLine line, Sound sound, GUI gui, RTPInterface rtpInterface) {
		if (!isValid(line) || line.incall) {
			return;
		}
		
		line.dial = "555";
		call(line, sound, gui, rtpInterface);
	}	
	

}
