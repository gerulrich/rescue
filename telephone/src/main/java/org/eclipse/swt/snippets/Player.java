package org.eclipse.swt.snippets;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player {

	/**
	 * @param args
	 */
	public void play(String file) {
		Clip sonido = null;
		try {
			sonido = AudioSystem.getClip();
			sonido.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\German\\Music\\custom-1329650608.532.wav")));
			sonido.start();
			
			do {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (sonido.isRunning());
			sonido.stop();
			
			
			
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sonido.close();
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
        AudioFileFormat.Type[] tipos = AudioSystem.getAudioFileTypes();

		for (AudioFileFormat.Type tipo : tipos)
            System.out.println(tipo.getExtension());

		Clip sonido = AudioSystem.getClip();
		
		sonido.open(AudioSystem.getAudioInputStream(new File("C:\\Users\\German\\Music\\custom-1329650608.532.wav")));
		
		long longitud = sonido.getMicrosecondLength();
		System.out.println(longitud/1000000f);
		
//		sonido.setFramePosition(260960/2);
		
		sonido.start();
		
		System.out.println("Level:"+sonido.getLevel());
		
		
		
		
		
		
		sonido.addLineListener(new LineListener() {
			
			@Override
			public void update(LineEvent event) {
				System.out.println(event.getFramePosition());
			}
		});
		
		Thread.sleep(1000);
		int i = 0;
		while (sonido.isRunning() && i < 5) {
			   Thread.sleep(1000);
			   i++;
		}
		sonido.stop();
		
		Thread.sleep(5000);
		
		sonido.start();
		
		   Thread.sleep(1000);
		while (sonido.isRunning()) {
			   Thread.sleep(1000);
		}
		
		sonido.close();



	}

}
