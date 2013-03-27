package net.cloudengine.widgets.sound;

public class Settings {
	
	public final static int SOUND_LINUX = 1;
	public final static int SOUND_JAVA = 2;
	public final static int SOUND_FLASH = 3;

	public final static int VIDEO_NONE = 0;
	public final static int VIDEO_LINUX = 1;
	public final static int VIDEO_FLASH = 3;
	
	private boolean resample441k = false;
	
	private String audioInput = "<default>", audioOutput = "<default>";
	private boolean keepAudioOpen = true;
	private boolean swVolForce = false;
	private boolean speakerMode = false;
	private int soundType = SOUND_JAVA;
	private int videoType = VIDEO_NONE;
	private int speakerThreshold = 1000;  //0-32k
	private int speakerDelay = 250;  //ms

	private boolean aa; // auto answer
	private boolean ac; // auto conference
	
	private String monitor = "555";
	
	public String host;
	public int port;
	public String username;
	public String password;
	
	public Settings(String username, String password, String host, int port) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public boolean isResample441k() {
		return resample441k;
	}

	public void setResample441k(boolean resample441k) {
		this.resample441k = resample441k;
	}

	public String getAudioInput() {
		return audioInput;
	}

	public void setAudioInput(String audioInput) {
		this.audioInput = audioInput;
	}

	public String getAudioOutput() {
		return audioOutput;
	}

	public void setAudioOutput(String audioOutput) {
		this.audioOutput = audioOutput;
	}

	public boolean isKeepAudioOpen() {
		return keepAudioOpen;
	}

	public void setKeepAudioOpen(boolean keepAudioOpen) {
		this.keepAudioOpen = keepAudioOpen;
	}

	public boolean isSwVolForce() {
		return swVolForce;
	}

	public void setSwVolForce(boolean swVolForce) {
		this.swVolForce = swVolForce;
	}

	public boolean isSpeakerMode() {
		return speakerMode;
	}

	public void setSpeakerMode(boolean speakerMode) {
		this.speakerMode = speakerMode;
	}

	public int getSoundType() {
		return soundType;
	}

	public void setSoundType(int soundType) {
		this.soundType = soundType;
	}

	public int getVideoType() {
		return videoType;
	}

	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}

	public int getSpeakerThreshold() {
		return speakerThreshold;
	}

	public void setSpeakerThreshold(int speakerThreshold) {
		this.speakerThreshold = speakerThreshold;
	}

	public int getSpeakerDelay() {
		return speakerDelay;
	}

	public void setSpeakerDelay(int speakerDelay) {
		this.speakerDelay = speakerDelay;
	}

	public boolean isAa() {
		return aa;
	}

	public void setAa(boolean aa) {
		this.aa = aa;
	}

	public boolean isAc() {
		return ac;
	}

	public void setAc(boolean ac) {
		this.ac = ac;
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
