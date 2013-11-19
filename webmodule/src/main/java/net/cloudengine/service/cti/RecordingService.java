package net.cloudengine.service.cti;

import java.io.OutputStream;

public interface RecordingService {
	
	boolean getAudioRecord(String filename, OutputStream out);

}
