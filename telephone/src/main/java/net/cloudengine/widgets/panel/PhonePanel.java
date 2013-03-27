package net.cloudengine.widgets.panel;

import java.util.Timer;

import javaforce.JF;
import javaforce.JFLog;
import javaforce.voip.Codec;
import javaforce.voip.RTP;
import javaforce.voip.RTPInterface;
import javaforce.voip.SIP;
import javaforce.voip.SIPClient;
import javaforce.voip.SIPClientInterface;
import javaforce.voip.g711;
import net.cloudengine.util.GuiUtil;
import net.cloudengine.widgets.sound.MeterController;
import net.cloudengine.widgets.sound.PhoneLine;
import net.cloudengine.widgets.sound.Settings;
import net.cloudengine.widgets.sound.Sound;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class PhonePanel extends Composite  implements MeterController, SIPClientInterface, GUI, RTPInterface {

	private static final int SPACE_HORIZONTAL = 5;
	
	private String lastDial;
	private PhoneLine lines[];
	private Sound sound = new Sound();
	private int line = -1;  //current selected line (0-5) (-1=none)
	
	private PhoneLine selectedLine = null;
	private Settings settings = null;
	
	private int registerRetries;
	private java.util.Timer timerKeepAlive, timerRegisterExpires, timerRegisterRetries;
	private int localport = 5061;
	
	private GUI gui;
	
	
	private EventInterceptor interceptor;
	
	//////////////////////////////////////
	
	private Label dial;
	private Label status;
	
	private LinesControl linesPanel;
	private AccionsControl accions;
	
	private Button endButton;
	private Button callButton;
	
	private MeterControl recMeter;
	private MeterControl playMeter;
	
	private Button transfer;
	private Button hold;
	private Button dnd;
	private Button aa;
	private Button redial;
	private Button monitor;
	
	public PhonePanel(Settings settings, EventInterceptor interceptor, Composite parent, int style) {
		super(parent, style);
		createGui(this, style);
		gui = this;
		this.settings = settings;
		init();
	}
	
	private void init() {
		lines = new PhoneLine[6];
		for(int i = 0; i < 6; i++) {
			lines[i] = new PhoneLine();
		}
		sound.init(settings, lines, this);
		
		new Thread() {
			public void run() {
				reRegisterAll();
		      }
		}.start();

		
		this.accions.getButtons()[5].addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				doMonitor();
			}
		});		
		
		this.accions.getButtons()[4].addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				toggleDND();
			}
		});
		
		this.accions.getButtons()[3].addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				toggleAA();
			}
		});
		
		this.accions.getButtons()[1].addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				doHold();
			}
		});
		
		this.accions.getButtons()[0].addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				doXfer();
			}
		});		
		
		for(int i = 0; i < linesPanel.getButtons().length; i++) {
			final int j = i; 
			linesPanel.getButtons()[i].addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					selectLine(j);
				}
			});
		}
		
		Timer buttontimer = new Timer();
	    buttontimer.schedule(new ButtonColorUpdater(lines, this.linesPanel.getButtons()), 0, 250);
	}
	
	private void createGui(Composite parent, int style) {
		this.setLayout(new FormLayout());
		
		dial = new Label(this, SWT.BORDER | SWT.SINGLE);
		FormData fd = new FormData();
	    fd.top = new FormAttachment(2, 2);
	    fd.left = new FormAttachment(0, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(95, SPACE_HORIZONTAL);
	    dial.setLayoutData(fd);
	    
	    status = new Label(this, SWT.BORDER | SWT.SINGLE);
	    fd = new FormData();
	    fd.top = new FormAttachment(dial, 5);
	    fd.left = new FormAttachment(0, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(95, SPACE_HORIZONTAL);
	    status.setLayoutData(fd);
	    
	    createLineButtons(parent);
	    
	    accions = new AccionsControl(parent, style);
	    fd = new FormData();
	    fd.top = new FormAttachment(linesPanel, 5);
	    fd.left = new FormAttachment(0, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(95, SPACE_HORIZONTAL);
	    accions.setLayoutData(fd);
	    
	    Label separator = new Label (this, SWT.SEPARATOR | SWT.HORIZONTAL);
	    fd = new FormData();
	    fd.top = new FormAttachment(accions, 5);
	    fd.left = new FormAttachment(0, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(95, SPACE_HORIZONTAL);
	    separator.setLayoutData(fd);
	    
	    callButton = new Button(parent, SWT.TOGGLE);
	    callButton.setText("Llamar");
	    fd = new FormData();
	    fd.top = new FormAttachment(separator, 5);
	    fd.left = new FormAttachment(0, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(45, SPACE_HORIZONTAL);
	    callButton.setLayoutData(fd);
	    
	    endButton = new Button(parent, SWT.TOGGLE);
	    endButton.setText("Colgar");
	    fd = new FormData();
	    fd.top = new FormAttachment(separator, 5);
	    fd.left = new FormAttachment(callButton, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(95, SPACE_HORIZONTAL);
	    endButton.setLayoutData(fd);
	    
	    recMeter = new MeterControl(this, style, true);
	    fd = new FormData();
	    fd.top = new FormAttachment(callButton, 5);
	    fd.left = new FormAttachment(0, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(30, SPACE_HORIZONTAL);
	    recMeter.setLayoutData(fd);

	    KeyPad pad = new KeyPad(this, style);
	    fd = new FormData();
	    fd.top = new FormAttachment(callButton, 5);
	    fd.left = new FormAttachment(recMeter, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(65, SPACE_HORIZONTAL);
	    pad.setLayoutData(fd);
	    
	    playMeter = new MeterControl(this, style, false); 
	    fd = new FormData();
	    fd.top = new FormAttachment(endButton, 5);
	    fd.left = new FormAttachment(pad, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(95, SPACE_HORIZONTAL);
	    playMeter.setLayoutData(fd);
	    
	    for(Button b: pad.getButtons()) {
	    	b.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					Button o = (Button)e.widget;
					SipUtils.releaseDigit(selectedLine, o.getText().charAt(0));
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					Button o = (Button)e.widget;
					SipUtils.pressDigit(selectedLine, o.getText().charAt(0));
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}
			});
	    	b.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event event) {
					Button o = (Button)event.widget;
					addDigit(o.getText().charAt(0));
				}
	    	});
	    }
	    
	    callButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				call();
			}
	    });
	    
	    endButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				end();
			}
	    });
	    
	    g711.init();  
	    
	    this.transfer = this.accions.getButtons()[0];
		this.hold = this.accions.getButtons()[1];
		this.redial = this.accions.getButtons()[2];
		this.aa = this.accions.getButtons()[3];
		this.dnd = this.accions.getButtons()[4];
		this.monitor = this.accions.getButtons()[5];
		
		transfer.setImage(Imagenes.getImage(Imagenes.PIC_GREY));
		hold.setImage(Imagenes.getImage(Imagenes.PIC_GREY));
		redial.setImage(Imagenes.getImage(Imagenes.PIC_GREY));
	    aa.setImage(Imagenes.getImage(Imagenes.PIC_GREY));
	    dnd.setImage(Imagenes.getImage(Imagenes.PIC_GREY));
	    monitor.setImage(Imagenes.getImage(Imagenes.PIC_GREY));

  
	}
	
	private void createLineButtons(Composite parent) {
		linesPanel = new LinesControl(parent, SWT.NONE);
		FormData fd = new FormData();
	    fd.top = new FormAttachment(status, 5);
	    fd.left = new FormAttachment(0, SPACE_HORIZONTAL);
	    fd.right = new FormAttachment(95, SPACE_HORIZONTAL);
	    linesPanel.setLayoutData(fd);
	}
	
	@Override
	public void dispose() {
		this.unRegisterAll();		
		super.dispose();
	}

	@Override
	public void setMeterRec(final int lvl) {
		recMeter.setMeterRec(lvl);
	}

	@Override
	public void setMeterPlay(final int lvl) {
		playMeter.setMeterPlay(lvl);
	}

	@Override
	public void setSpeakerStatus(boolean state) {
		playMeter.setSpeakerStatus(state);
	}
	
	@Override
	public void onRegister(SIPClient sip, boolean status) {
		if (status) {
			//success
			for(int a=0;a<6;a++) {
				if (lines[a].sip != sip) continue;
				if (lines[a].status.length() == 0) lines[a].status = "Ready (" + lines[a].user + ")";
				lines[a].auth = true;
				if (line == -1) {
					gui.selectLine(a);
				} else {
					if (line == a) 
						gui.updateLine();
				}
			}
			sip.subscribe(sip.getUser(), "message-summary", 3600);  //SUBSCRIBE to self for message-summary event (not needed with Asterisk but X-Lite does it)
			gui.onRegister(sip);
		} else {
			//failed
			for(int a=0;a<6;a++) {
				if (lines[a].sip == sip) {
					lines[a].status = Estados.UNAUTHORIZED;
					lines[a].unauth = true;
					if (line == a) gui.selectLine(-1);
				}
			}
		}
	}

	@Override
	public void onTrying(SIPClient client, String callid) {
		for(int a=0;a<6;a++) {
			if ((lines[a].incall)&&(!lines[a].trying)) {
				if (lines[a].callid.equals(callid)) {
					lines[a].trying = true;
					lines[a].status = Estados.TRYING;
					if (line == a) gui.updateLine();
				}
			}
		}
	}

	@Override
	public void onRinging(SIPClient client, String callid) {
		//is a line trying to do an invite
		PhoneLine line = SipUtils.findByCallId(lines, callid);
		if (line != null) {
			line.ringing = true;
			line.status = Estados.RINGING;
			if (line == selectedLine) 
				gui.updateLine();
		}
	}

	@Override
	public void onSuccess(SIPClient client, String callid,
			String remotertphost, int remotertpport, int remoteVrtpport,
			Codec[] codecs) {

	    for(int a=0;a<codecs.length;a++) {
	        JFLog.log("onSuccess : codecs[] = " + codecs[a].name + ":" + codecs[a].id);
	      }
	      if (remotertphost == null) return;
	      //is a line trying to do an invite or hold
	      for(int a=0;a<6;a++) {
	        if (!lines[a].incall) continue;
	        if (!lines[a].callid.equals(callid)) continue;
	        if (!lines[a].talking) {
	          if (SIP.hasCodec(codecs, RTP.CODEC_G711u) && SIP.hasCodec(codecs, RTP.CODEC_G729a)) {
	            //try to reinvite without g711u
	            lines[a].sip.reinvite(callid, lines[a].rtp.getlocalrtpport(), SIP.delCodec(codecs, RTP.CODEC_G711u));
	            return;
	          }
	          lines[a].codecs = codecs;
	          lines[a].status = "Connected";
	          if (line == a) gui.updateLine();
	          callInviteSuccess(a, remotertphost, remotertpport, remoteVrtpport);
	          lines[a].talking = true;
	          lines[a].ringing = false;
	          return;
	        }
	        if (lines[a].hld) {
	          lines[a].rtp.hold(true);
	        } else {
	          lines[a].rtp.hold(false);
	        }
	        return;
	      }
	      JFLog.log("err:ok for no call:" + callid);
		
	}

	@Override
	public void onBye(SIPClient client, String callid) {
		PhoneLine line = SipUtils.findByCallId(lines, callid);
		if (line != null) {
			//FIXME
		}
		for(int a=0;a<6;a++) {
			if (lines[a].incall) {
				if (lines[a].callid.equals(callid)) {
					endLine(a);
					// FIXME
//	            updateIconTray();
				}
			}
		}
		if (interceptor != null) {
			interceptor.onCallEnd();
		}
	}

	@Override
	public int onInvite(SIPClient sip, String callid, String fromid, String fromnumber, 
			String remotertphost, int remotertpport,
			int remoteVrtpport, Codec[] codecs) {

	    //NOTE : onInvite() can not change codecs (use SIP.accept() to do that)
	    for(int a=0;a<codecs.length;a++) {
	      JFLog.log("onInvite : codecs[] = " + codecs[a].name + ":" + codecs[a].id);
	    }
	    for(int a=0;a<6;a++) {
	      if (lines[a].sip == sip) {
	        if (lines[a].incall) {
	          if (lines[a].callid.equals(callid)) {
	            //reINVITEd (usually to change RTP host/port) (codec should not change since we only accept 1 codec)
	            lines[a].codecs = codecs;
	            lines[a].remotertphost = remotertphost;
	            lines[a].remotertpport = remotertpport;
	            lines[a].remoteVrtpport = remoteVrtpport;
	            lines[a].rtp.change(remotertphost, remotertpport);
	            lines[a].rtp.change(codecs);
	            if (remoteVrtpport != -1) {
	              lines[a].Vrtp.change(remotertphost, remoteVrtpport);
	              lines[a].Vrtp.change(codecs);
	            }
	            return 200;
	          }
	          continue;
	        }
	        lines[a].dial = fromnumber;
	        lines[a].callerid = fromid;
	        if ((lines[a].callerid == null) || (lines[a].callerid.trim().length() == 0)) lines[a].callerid = "Unknown";
//	        Settings.addCallLog(lines[a].dial);
//	        gui.updateRecentList();
	        lines[a].status = fromid + " is calling";
	        lines[a].incoming = true;
	        lines[a].remotertphost = remotertphost;
	        lines[a].remotertpport = remotertpport;
	        lines[a].remoteVrtpport = remoteVrtpport;
	        lines[a].callid = callid;
	        lines[a].ringing = true;
	        lines[a].codecs = codecs;
	        if (settings.isAa()) {
	          gui.selectLine(a);
	          gui.updateLine();
	          call();  //this will send a reply
	          return -1;  //do NOT send a reply
	        } else {
	          if (line == a) gui.updateLine();
	        }
//	        updateIconTray();
	        return 180;  //reply RINGING
	      }
	    }
	    return 486;  //reply BUSY		
	}
	
	
	/** Empieza una nueva llamada o acepta una 
	 * llamada entrante. 
	 */
	public void call() {
		SipUtils.call(selectedLine, sound, this, this);
	}
	
	private void end() {
		SipUtils.end(selectedLine, gui);
	}	
	
	@Override
	public void onCancel(SIPClient client, String callid, int code) {
		for(int a = 0; a < 6; a++) {
			if (lines[a].callid.equals(callid)) {
				lines[a].dial = "";
				lines[a].status = "Hungup (" + code + ")";
				lines[a].ringing = false;
				lines[a].incoming = false;
				lines[a].incall = false;
				if (line == a) gui.updateLine();
			}
		}
	}

	@Override
	public void onRefer(SIPClient client, String callid) {
		for(int a = 0; a < 6; a++) {
			if (lines[a].callid.equals(callid)) {
				endLine(a);
			}
		}
	}

	@Override
	public void onNotify(SIPClient sip, String event, String content) {
//	    JFLog.log("notify()");
		String contentLines[] = content.split("\r\n");
		if (event.equals("message-summary")) {
			// mensajes del contestador automatico.
			String msgwait = SIP.getHeader("Messages-Waiting:", contentLines);
			if (msgwait != null) {
				for(int a=0;a<6;a++) {
					if (lines[a].sip == sip) {
//	            JFLog.log("notify() line=" + a + ", msgwaiting = " + msgwaiting);
						lines[a].msgwaiting = msgwait.equalsIgnoreCase("yes");
					}
				}
			}
			return;
		}

	}

	@Override
	public String getResponse(SIPClient client, String realm, String cmd, String uri, String nonce, String qop, String nc, String cnonce) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Selecciona una linea y actualiza la gui.
	 */
	@Override
	public void selectLine(int newline) {
		// make sure line is valid
		linesPanel.unselectAll();
		
		if ((line != -1) && (lines[line].dtmf != 'x')) {
			lines[line].dtmfend = true;
		}
		
		line = newline;
		if (newline >= 0) {
			selectedLine = lines[newline]; 
		} else {
			selectedLine = null;
		}
		
		if (line == -1) {
			dial.setText("");
			status.setText("");
			
			GuiUtil.changeImage(hold, Imagenes.getImage(Imagenes.PIC_GREY));
			GuiUtil.changeImage(dnd, Imagenes.getImage(Imagenes.PIC_GREY));
			return;
		}
		
		linesPanel.selectLine(line);
		sound.selectLine(line);
		updateLine();
	}

	@Override
	public void updateLine() {
		this.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				endButton.setSelection(false);
			    if (lines[line].incall) {
			    	callButton.setSelection(true);
			    } else {
			    	callButton.setSelection(false);
			    }
			    dial.setText(lines[line].dial);
			    status.setText(lines[line].status);
			    
			    Image image = Imagenes.getImage(lines[line].hld ? Imagenes.PIC_RED : Imagenes.PIC_GREY);
			    GuiUtil.changeImage(hold, image);
			    
			    image = Imagenes.getImage(lines[line].dnd ? Imagenes.PIC_RED : Imagenes.PIC_GREY);
			    GuiUtil.changeImage(dnd, image);

			    image = Imagenes.getImage(lines[line].xfer ? Imagenes.PIC_GREEN : Imagenes.PIC_GREY);
			    transfer.setImage(image);
			}
		});
	}
	
	@Override
	public void updateCallButton(final boolean state) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				callButton.setSelection(state);
			}
		});
	}

	@Override
	public void updateEndButton(final boolean state) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				endButton.setSelection(state);
			}
		});
	}

	@Override
	public void endLineUpdate(int xline) {
		//FIXME no se usa eliminar el método de la interface		
		// no se usa la publicación de estado.
//		if ((Settings.current.usePublish) && (presenceStatus.getSelectedIndex() == 0)) lines[xline].sip.publish("open");
	}
	
	@Override
	public void endLineUpdate(PhoneLine line) {
		//FIXME no se usa eliminar el método de la interface
		// no se usa la publicación de estado.
//		if ((Settings.current.usePublish) && (presenceStatus.getSelectedIndex() == 0)) lines[xline].sip.publish("open");
	}
	
	

	@Override
	public void callInviteUpdate() {
		// no se usa la publicacion de estado no lista de llamadas recientes.
//		updateRecentList();
//	    if ((Settings.current.usePublish) && (presenceStatus.getSelectedIndex() == 0)) lines[line].sip.publish("busy");
	}

	@Override
	public void hld_setIcon(Image image) {
		GuiUtil.changeImage(hold, image);
	}

	@Override
	public void aa_setIcon(Image image) {
		GuiUtil.changeImage(aa, image);
	}

	@Override
	public void ac_setIcon(Image image) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dnd_setIcon(Image image) {
		GuiUtil.changeImage(dnd, image);
	}

	@Override
	public void cnf_setIcon(Image image) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mute_setIcon(Image image) {
		this.recMeter.setImageButton(image);
	}

	@Override
	public void spk_setIcon(Image image) {
		this.playMeter.setImageButton(image);
	}

	@Override
	public void onRegister(SIPClient sip) {
		// TODO Auto-generated method stub
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	public void reRegisterAll() {
	    
		String host = settings.getHost();
		int port = settings.getPort();
		String username = settings.getUsername();
		String password = settings.getPassword();
	    

//	    lines = new PhoneLine[6];
	    
	    for(int a=0;a<1;a++) {
//	      if ((a > 0) && (Settings.current.lines[a].same != -1)) continue;
//	      if (Settings.current.lines[a].host.length() == 0) continue;
//	      if (Settings.current.lines[a].user.length() == 0) continue;
//	    	lines[a] = new PhoneLine();
	    	lines[a].sip = new CustomSIP();
	      
//	      idx = Settings.current.lines[a].host.indexOf(':');
//	      if (idx == -1) {
//	        host = Settings.current.lines[a].host;
//	        port = 5060;
//	      } else {
//	        host = Settings.current.lines[a].host.substring(0,idx);
//	        port = JF.atoi(Settings.current.lines[a].host.substring(idx+1));
//	      }
	      
	      int attempt = 0;
	      while (!lines[a].sip.init(host, port, localport++, this)) {
	        attempt++;
	        if (attempt==10) {
	          lines[a].sip = null;
	          lines[a].status = "SIP init failed";
	          if (a == line) gui.updateLine();
	          break;
	        }
	      }
	      
	      if (lines[a].sip == null) continue;  //sip.init failed
	      
	      lines[a].user = username;//Settings.current.lines[a].user;
	
	      //JFLog.log("lines[" + a + "].pass=" + Settings.current.lines[a].pass + "!");
	      
//	      if ((Settings.current.lines[a].pass == null) || (Settings.current.lines[a].pass.length() == 0) || (Settings.current.lines[a].pass.equals("crypto(1,)"))) {
//	        lines[a].auth = true;
//	        lines[a].noregister = true;
//	        lines[a].status = "Ready (" + lines[a].user + ")";
//	        continue;
//	      }
	      
	    }
	    
	    //setup "Same as" lines
	    int same = 0;
	    for(int a=1;a<6;a++) {
//	      same = Settings.current.lines[a].same;
//	      if (same == -1) continue;
	     
//	      lines[a] = new PhoneLine();
	    	
	      lines[a].sip = lines[same].sip;
	      lines[a].user = lines[same].user;
	      lines[a].noregister = lines[same].noregister;
	      if (lines[a].noregister) {
	        lines[a].auth = true;
	        lines[a].status = "Ready (" + lines[a].user + ")";
	      }
	    }
	    
	    //register lines
	    for(int a=0;a<1;a++) {
//	      if ((a > 0) && (Settings.current.lines[a].same != -1)) continue;
	      if (lines[a].sip == null) continue;
	      try {
	    	lines[a].sip.register(username, username, password);
//	        lines[a].sip.register(Settings.current.lines[a].user, Settings.current.lines[a].auth, Settings.getPassword(Settings.current.lines[a].pass));
	      } catch (Exception e) {
	        JFLog.log(e);
	      }
	    }
	    //setup reRegister timer (expires)
	    timerRegisterExpires = new java.util.Timer();
	    timerRegisterExpires.scheduleAtFixedRate(new ReRegisterExpires(), 3595*1000, 3595*1000);  //do it 5 seconds early just to be sure
	    registerRetries = 0;
	    timerRegisterRetries = new java.util.Timer();
	    timerRegisterRetries.schedule(new ReRegisterRetries(), 1000);
	  }
	

	  /** Expires registration with all SIP connections. */

	public void unRegisterAll() {
		if (timerRegisterExpires != null) {
			timerRegisterExpires.cancel();
			timerRegisterExpires = null;
		}
		if (timerRegisterRetries != null) {
			timerRegisterRetries.cancel();
			timerRegisterRetries = null;
		}
		for(int a=0;a<6;a++) {
			if (lines[a].incall) {
				gui.selectLine(a);
				end();
			}
			lines[a].dial = "";
			lines[a].status = "";
			lines[a].unauth = false;
			lines[a].auth = false;
			lines[a].noregister = false;
			lines[a].user = "";
			if ( a > 0 ) {
				lines[a].sip = null;
				continue;
			}
			if (lines[a].sip == null) continue;
			if (lines[a].sip.isRegistered()) {
				try {
					lines[a].sip.unregister();
				} catch (Exception e) {
					JFLog.log(e);
				}
			}
		}
		int maxwait;
		for(int a=0;a<6;a++) {
			if (lines[a].sip == null) continue;
			maxwait = 1000;
			while (lines[a].sip.isRegistered()) { JF.sleep(10); maxwait -= 10; if (maxwait == 0) break; }
			lines[a].sip.uninit();
			lines[a].sip = null;
		}
	}
	
	
	public class ReRegisterExpires extends java.util.TimerTask {
		public void run() {
			for(int a=0;a<1;a++) {
//	        if (Settings.current.lines[a].same != -1) continue;
				if (lines[a].sip == null) continue;
				if (lines[a].noregister) continue;
				lines[a].sip.reregister();
			}
			registerRetries = 0;
			if (timerRegisterRetries != null) {
				timerRegisterRetries = new java.util.Timer();
				timerRegisterRetries.schedule(new ReRegisterRetries(), 1000);
			}
		}
	}

	/** TimerTask that reregisters any SIP connections that have failed to register (checks every 1 second upto 5 attempts). */
	public class ReRegisterRetries extends java.util.TimerTask {
		
		public void run() {
			boolean again = false;
			for(int a=0;a<1;a++) {
//	        if (Settings.current.lines[a].same != -1) continue;
				if (lines[a].sip == null) continue;
				if (lines[a].unauth) continue;
				if (lines[a].noregister) continue;
				if (!lines[a].sip.isRegistered()) {
					JFLog.log("warn:retry register on line:" + (a+1));
					lines[a].sip.reregister();
					again = true;
				}
			}
			registerRetries++;
			if ((again) && (registerRetries < 5)) {
				timerRegisterRetries = new java.util.Timer();
				timerRegisterRetries.schedule(new ReRegisterRetries(), 1000);
			} else {
				for(int a=0;a<1;a++) {
//	          if (Settings.current.lines[a].same != -1) continue;
					if (lines[a].sip == null) continue;
					if (lines[a].unauth) continue;
					if (lines[a].noregister) continue;
					if (!lines[a].sip.isRegistered()) {
						lines[a].unauth = true;  //server not responding after 5 attempts to register
					}
				}
				timerRegisterRetries = null;
			}
		}
	  }

	//////////////////////////////////////////////

	@Override
	public void rtpSamples(RTP rtp) { }

	@Override
	public void rtpDigit(RTP rtp, char digit) { }

	@Override
	public void rtpPacket(RTP rtp, boolean rtcp, byte[] data, int off, int len) { }

	@Override
	public void rtpH263(RTP rtp, byte[] data, int off, int len) { }

	@Override
	public void rtpH264(RTP rtp, byte[] data, int off, int len) { }

	@Override
	public void rtpJPEG(RTP rtp, byte[] data, int off, int len) { }

	@Override
	public void rtpFLV(RTP rtp, byte[] data, int off, int len) { }
	
	////////////////////////////////////////////////////////////////////
	
	/** Agrega un digito para ser marcado */
	public void addDigit(char digit) {
		SipUtils.addDigit(selectedLine, this, digit);
	}
	
	/** Cleanup after a call is terminated (call terminated local or remote). */
	private void endLine(int xline) {
		gui.updateEndButton(false);
		gui.updateCallButton(false);
		
		lines[xline].dial = "";
		lines[xline].status = Estados.HUNGUP;
		lines[xline].trying = false;
		lines[xline].ringing = false;
		lines[xline].incoming = false;
		lines[xline].hld = false;
		lines[xline].cnf = false;
		lines[xline].xfer = false;
		lines[xline].incall = false;
		lines[xline].talking = false;
		lines[xline].rtp.stop();
		lines[xline].rtp = null;
		lines[xline].Vrtp = null;
		lines[xline].callid = "";

		gui.updateLine();
		gui.endLineUpdate(xline);
	}


	private void callInviteSuccess(int xline, String remotertphost, int remotertpport, int remoteVrtpport) {
		JFLog.log("callInviteSuccess:remotertpport=" + remotertpport + ",remoteVrtpprt=" + remoteVrtpport);
		
		if (!SIP.hasCodec(lines[xline].codecs, RTP.CODEC_G729a) && !SIP.hasCodec(lines[xline].codecs, RTP.CODEC_G711u)) {
			//BUG : Should cancel call???
			JFLog.log("callInviteSuccess() Failed : No compatible audio codec returned");
			return;
		}

		lines[xline].remotertphost = remotertphost;
		lines[xline].remotertpport = remotertpport;
		lines[xline].remoteVrtpport = remoteVrtpport;
		sound.flush();
		lines[xline].rtp.start(remotertphost, remotertpport, lines[xline].codecs, false);
		if (remoteVrtpport != -1) {
			lines[xline].Vrtp.start(remotertphost, remoteVrtpport, lines[xline].codecs, true);
		}
		
		if (settings.isAa()) 
			gui.selectLine(xline);
	}
	
	
	/** Comienza o finaliza una transferencia de una llamada */ 
	private void doXfer() {
		SipUtils.doXfer(selectedLine, gui);
	}	
	
	
	private void doHold() {
		SipUtils.doHold(selectedLine, this);
	}
	
	private void toggleAA() {
		SipUtils.toggleAA(settings, this);
	}
	
	private void toggleDND() {
		SipUtils.toggleDND(selectedLine, sound, this, this);
	}
	
	private void doMonitor() {
		SipUtils.doMonitor(selectedLine, sound, this, this);
	}
}
