package net.cloudengine.service;

import java.io.OutputStream;

public interface RecordingService {
	
	boolean getAudioRecord(String filename, OutputStream out);

}
