package net.cloudengine.widgets.sound;
import java.util.Timer;
import java.util.TimerTask;

import javaforce.JFLog;
import javaforce.jni.JNI;
import javaforce.voip.DTMF;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/** Maneja todos los aspectos del procesamiento de sonido
 * (recording, playback, ringing sounds, conference mixing, etc.) */

public class Sound {
  
	//sound data
	private SourceDataLine sdl;
	private TargetDataLine tdl;
	private FloatControl sdlvol, tdlvol;
	private AudioFormat audioFormat;
//	private short silence[] = new short[160];
	private short mixed[] = new short[160];
	private Timer timer;
	private PhoneLine lines[];
	private int line = -1;
	private short data[] = new short[160];
	private boolean inRinging = false, outRinging = false;
	private MeterController mc;
	private boolean swVolPlay, swVolRec;
	private int volPlay = 100, volRec = 100;
	private boolean mute = false;
	private DTMF dtmf = new DTMF();
//	private int writeCnt = 0;
	private boolean playing = false;
//	private Wav wav;
	private int speakerDelay = 0;
	private SoundUtil soundUtil = new SoundUtil();
	private Settings settings;
	
  
	int snd_id_play = -1, snd_id_record = -1;

  /** Incia el sistema de sonido.
   * Esta clase necesita acceso a las {@link PhoneLine} y a {@link MeterControl}
   * para enviar los niveles de sonido hacia la gui.
   */
  public boolean init(Settings settings, PhoneLine lines[], MeterController mc) {
	  this.lines = lines;
	  this.mc = mc;
	  this.settings = settings;
    
	  if (!init_java()) return false;
    
	  timer = new Timer();
	  timer.scheduleAtFixedRate(new TimerTask() {
		  public void run() {
			  process();
		  }
	  }, 0, 20);
	  return true;
  }

  /** Incia el sistema de sonido para java. */
  private boolean init_java() {
	  //JFLog.log("=== init_java() ===");
	  int idx;
	  try {
		  //format = float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian
		  audioFormat = new AudioFormat((settings.isResample441k() ? 44100.0f : 8000.0f), 16, 1, true, true);
		  Mixer.Info mi[] = AudioSystem.getMixerInfo();
		  idx = -1;
		  for(int a=0;a<mi.length;a++) {
			  if (mi[a].getName().equalsIgnoreCase(settings.getAudioOutput())) {
				  idx = a;
				  break;
			  }
		  }
		  if ( (settings.getAudioOutput().equalsIgnoreCase("<default>")) || (idx == -1) ) {
			  sdl = AudioSystem.getSourceDataLine(audioFormat);
		  } else {
			  sdl = AudioSystem.getSourceDataLine(audioFormat, mi[idx]);
		  }
		  if (sdl == null) throw new Exception("unable to get playback device");
		  idx = -1;
		  for(int a=0;a<mi.length;a++) {
			  if (mi[a].getName().equalsIgnoreCase(settings.getAudioInput())) {
				  idx = a;
				  break;
			  }
		  }
		  if ( (settings.getAudioInput().equalsIgnoreCase("<default>")) || (idx == -1) ) {
			  tdl = AudioSystem.getTargetDataLine(audioFormat);
		  } else {
			  tdl = AudioSystem.getTargetDataLine(audioFormat, mi[idx]);
		  }
		  if (tdl == null) throw new Exception("unable to get recording device");
		  sdl.open(audioFormat);
		  if (settings.isKeepAudioOpen()) sdl.start();
		  tdl.open(audioFormat);
		  tdl.start();
	  } catch (Exception e) {
//      JFLog.log("err:sound init: " + e); // TODO
		  return false;
	  }
	  swVolPlay = false;
	  try {
		  if (!settings.isSwVolForce()) {
			  sdlvol = (FloatControl) sdl.getControl(FloatControl.Type.VOLUME);
			  if (sdlvol == null)
				  throw new Exception("unable to get playback volume control");
		  } else {
			  swVolPlay = true;
		  }
	  } catch (Exception e1) {
		  try {
			  sdlvol = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
			  if (sdlvol == null) throw new Exception("unable to get playback volume control");
		  } catch (Exception w1) {
//        JFLog.log("warning:sound:unable to use hardware playing volume:using software mixing");
    	  // TODO
			  swVolPlay = true;
		  }
    }
    swVolRec = false;
    try {
      if (!settings.isSwVolForce()) {
        tdlvol = (FloatControl) tdl.getControl(FloatControl.Type.VOLUME);
        if (tdlvol == null)
          throw new Exception("unable to get recording volume control");
      } else {
        swVolRec = true;
      }
    } catch (Exception e2) {
      try {
        tdlvol = (FloatControl) tdl.getControl(FloatControl.Type.MASTER_GAIN);
        if (tdlvol == null) throw new Exception("unable to get recording volume control");
      } catch (Exception w2) {
//        JFLog.log("warning:sound:unable to use hardware recording volume:using software mixing");
        swVolRec = true;
      }
    }

//    JFLog.log("note:sound:init ok\r\nplayFormat=" + sdl.getFormat() + "\r\nrecFormat=" + tdl.getFormat());
    return true;
  }

  /** Frees resources. */

  public void uninit() {
	  if (timer != null) {
		  timer.cancel();
		  timer = null;
	  }
	  uninit_java();
  }

  private void uninit_java() {
	  if (sdl != null) {
		  try {
			  if (settings.isKeepAudioOpen()) {
				  sdl.stop();
			  } else {
				  if (playing) {
					  sdl.stop();
					  playing = false;
					  mc.setMeterPlay(0);
				  }
			  }
			  sdl.close();
		  } catch (Exception e) {
			  
		  }
		  sdl = null;
	  }
    
	  if (tdl != null) {
		  try {
			  tdl.stop();
			  tdl.close();
		  } catch (Exception e) {
			  
		  }
		  tdl = null;
	  }
  }

  /** Returns if software volume control on recording. */

  public boolean isSWVolRec() {
	  return swVolRec;
  }

  /** Returns if software volume control on playing. */

  public boolean isSWVolPlay() {
	  return swVolPlay;
  }

  /** Changes which line user wants to listen to. */

  public void selectLine(int line) {
	  this.line = line;
  }

  /** Changes software/hardware playback volume level. */
  public void setVolPlay(int lvl) {
	  volPlay = lvl;
	  if (swVolPlay) return;
	  float flvl = lvl/100.0f;
//    System.out.println("play flvl = " + flvl);
	  if (sdlvol == null) return;
	  sdlvol.setValue(flvl);
  }

  /** Changes software/hardware recording volume level. */

  public void setVolRec(int lvl) {
	  volRec = lvl;
	  if (swVolRec) return;
	  float flvl = lvl/100.0f;
//    System.out.println("rec flvl = " + flvl);
	  if (tdlvol == null) return;
	  tdlvol.setValue(flvl);
  }

  /** Sets mute state. */
  public void setMute(boolean state) {
	  mute = state;
  }

  /** Writes data to the audio system (output to speakers). */
  private void write(short buf[]) {
	  if (swVolPlay)  {
		  SoundUtil.scaleBuffer(buf, 0, 160, volPlay);
	  }
	  write_java(buf); 

	  int lvl = 0;
	  for (int a = 0; a < 160; a++) {
		if (Math.abs(buf[a]) > lvl) lvl = Math.abs(buf[a]);
	  }
	  mc.setMeterPlay(lvl * 100 / 32768);
	  if ((settings.isSpeakerMode()) && (lvl >= settings.getSpeakerThreshold())) {
		  if (speakerDelay <= 0) {
			  mc.setSpeakerStatus(false);
		  }
		  speakerDelay = settings.getSpeakerDelay();
	  }
  }

  
  private void write_java(short buf[]) {
	  if (sdl != null) {
		  sdl.write(SoundUtil.short2byte(buf, settings.isResample441k()), 0, (settings.isResample441k() ? 882 * 2 : 160 * 2));
	  }
  }

  
  /** Reads data from the audio system (input from mic). */
  private boolean read(short buf[]) {
	  if (!read_java(buf)) 
		  return false;
	  
	  if (swVolPlay) {
		  SoundUtil.scaleBuffer(buf, 0, 160, volRec);
	  }
	  int lvl = 0;
	  for (int a = 0; a < 160; a++) {
		  if (Math.abs(buf[a]) > lvl) lvl = Math.abs(buf[a]);
	  }
	  mc.setMeterRec(lvl * 100 / 32768);
	  if (speakerDelay > 0) {
		  speakerDelay -= 20;
		  System.arraycopy(soundUtil.getSilence(), 0, buf, 0, 160);
		  if (speakerDelay <= 0) {
			  mc.setSpeakerStatus(true);
		  }
	  }
	  return true;
  }

  private boolean read_java(short buf[]) {
	  byte buf8[];
	  int ret;
	  if (tdl == null) return true;
	  if (settings.isResample441k()) {
		  if (tdl.available() < (882 * 2)) return false;  //do not block (causes audio glitches)
		  buf8 = new byte[882 * 2];
		  ret = tdl.read(buf8, 0, 882 * 2);
		  if (ret != 882 * 2) return false;
	  } else {
		  if (tdl.available() < (160 * 2)) return false;  //do not block (causes audio glitches)
		  buf8 = new byte[160 * 2];
		  ret = tdl.read(buf8, 0, 160 * 2);
		  if (ret != 160 * 2) return false;
	  }
	  SoundUtil.byte2short(buf8, buf, settings.isResample441k());
	  return true;
  }

  /** Flushes output buffers.  Should be called at start of a call. */
  public void flush() {
	  try {
		  sdl.flush();
	  } catch (Exception e) {
//          JFLog.log(e); TODO
	  }
  }

  /** Timer event that is triggered every 20ms.  Processes playback / recording. */
  public void process() {
	  //20ms timer
	  //do playback
	  try {
		  int cc = 0;  //conf count
		  byte encoded[];
		  if (!settings.isKeepAudioOpen()) {
			  if (!playing) {
				  for (int a = 0; a < 6; a++) {
					  if ((lines[a].talking) || (lines[a].ringing)) {
						  playing = true;
						  if (sdl != null) sdl.start();
						  if (snd_id_play != -1) JNI.gst_resume(snd_id_play);
						  break;
					  }
				  }
			  } else {
				  int pc = 0;  //playing count
				  for (int a = 0; a < 6; a++) {
					  if ((lines[a].talking) || (lines[a].ringing)) {
						  pc++;
					  }
				  }
				  if (pc == 0) {
					  playing = false;
					  mc.setMeterPlay(0);
					  if (sdl != null) sdl.stop();
					  if (snd_id_play != -1) JNI.gst_resume(snd_id_play);
				  }
			  }
		  }
		  for (int a = 0; a < 6; a++) {
			  if (lines[a].talking) {
				  if ((lines[a].cnf) && (!lines[a].hld)) cc++;
			  }
		  }
		  for (int a = 0; a < 6; a++) {
			  if (lines[a].ringing) {
				  if (!outRinging) {
					  soundUtil.startRinging();
					  outRinging = true;
				  }
				  break;
			  }
			  if (a == 5) {
				  outRinging = false;
			  }
		  }
		  for (int a = 0; a < 6; a++) {
			  if (lines[a].incoming) {
				  if (!inRinging) {
//					  if (wav.isLoaded()) {
//						  wav.reset();
//					  } else {
						  soundUtil.startRinging();
//					  }
					  inRinging = true;
				  }
				  break;
        }
        if (a == 5) {
          inRinging = false;
        }
      }
      if ((cc > 1) && (line != -1) && (lines[line].cnf)) {
        //conference mode
        System.arraycopy(soundUtil.getSilence(), 0, mixed, 0, 160);
        for (int a = 0; a < 6; a++) {
          if ((lines[a].talking) && (lines[a].cnf) && (!lines[a].hld) && (lines[a].rtp.getSamples(lines[a].samples))) {
            mix(mixed, lines[a].samples);
          }
        }
        
        if (inRinging) 
        	mix(mixed, soundUtil.getCallWaiting());
        if (lines[line].dtmf != 'x') 
        	mix(mixed, dtmf.getSamples(lines[line].dtmf));
        write(mixed);
        
      } else {
        //single mode
        System.arraycopy(soundUtil.getSilence(), 0, mixed, 0, 160);
        if (line != -1) {
          if (lines[line].dtmf != 'x') mix(mixed, dtmf.getSamples(lines[line].dtmf));
        }
        if ((line != -1) && (lines[line].talking) && (!lines[line].hld)) {
          if (lines[line].rtp.getSamples(data)) mix(mixed, data);
          if (inRinging) mix(mixed, soundUtil.getCallWaiting());
          write(mixed);
        } else {
          if (inRinging || outRinging) mix(mixed, soundUtil.getRinging());
          if ((playing) || (settings.isKeepAudioOpen())) write(mixed);
        }
      }
//  if (!Settings.isApplet) JFLog.log("Sound.process() : " + sdl.getFramePosition() + ":" + sdl.getMicrosecondPosition() + ":" + sdl.available() + ":" + System.currentTimeMillis() + ":" + writeCnt );
      //do recording
      boolean readstatus = read(data);
      if ((mute) || (!readstatus)) System.arraycopy(soundUtil.getSilence(), 0, data, 0, 160);
      for (int a = 0; a < 6; a++) {
    	  if ((lines[a].talking) && (!lines[a].hld)) {
          if ((lines[a].cnf) && (cc > 1)) {
            //conference mode (mix = data + all other cnf lines except this one)
            System.arraycopy(data, 0, mixed, 0, 160);
            for (int b = 0; b < 6; b++) {
              if (b == a) continue;
              if ((lines[b].talking) && (lines[b].cnf) && (!lines[b].hld)) mix(mixed, lines[b].samples);
            }
            encoded = lines[a].rtp.coder.encode(mixed, 0, 160);
          } else {
            //single mode
            if (line == a)
              encoded = lines[a].rtp.coder.encode(data, 0, 160);
            else
              encoded = lines[a].rtp.coder.encode(soundUtil.getSilence(), 0, 160);
          }
          if (lines[a].dtmfend) {
            lines[a].rtp.getDefaultChannel().writeDTMF(lines[a].dtmf, true);
          } else if (lines[a].dtmf != 'x') {
            lines[a].rtp.getDefaultChannel().writeDTMF(lines[a].dtmf, false);
          } else {
            lines[a].rtp.getDefaultChannel().writeRTP(encoded,0,encoded.length);
//            writeCnt++;
          }
        }
        if (lines[a].dtmfend) {
          lines[a].dtmfend = false;
          lines[a].dtmf = 'x';
        }
      }
    } catch (Exception e) {
      JFLog.log(e);
      // TODO
    }
  }

  /** Mixes 'in' samples into 'out' samples. */
  public void mix(short out[], short in[]) {
	  for (int a = 0; a < 160; a++) {
		  out[a] += in[a];
	  }
  }
}
