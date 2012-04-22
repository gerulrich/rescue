package org.eclipse.swt.snippets;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshSample1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		JSch jsch = new JSch();

	    Session sesion = jsch.getSession("root", "192.168.0.104", 22);
	    sesion.setPassword("pandora");

	    Properties config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    sesion.setConfig(config);

	    sesion.connect();
	    
	    // ==========================================
	    ejecutarComando(sesion, "ls /");
	    descargarArchivo(sesion, "custom-1329677354.709.wav");
	    Player p = new Player();
	    p.play("C:\\Users\\German\\custom-1329677354.709.wav");
	    sesion.disconnect();
		
	}
	
	private static void ejecutarComando(Session sesion, String comando) throws Exception {
	    Channel canal = sesion.openChannel("exec");
	    String resultado = null;
	    
	    ((ChannelExec)canal).setCommand(comando);
	    ((ChannelExec)canal).setErrStream(System.err);

	    InputStream in = canal.getInputStream();

	    canal.connect();

	    byte[] tmp = new byte[1024];
	    while (true) {
	      if (in.available() > 0) {
	        int i = in.read(tmp, 0, 1024);
	        if (i >= 0)
	        {
	          resultado = new String(tmp, 0, i);
	          continue;
	        }
	      }if (canal.isClosed()) {
	        break;
	      }

	    }

	    canal.disconnect();

	    System.out.println(resultado);		
	}
	
	private static void descargarArchivo(Session sesion, String grabacion) throws Exception {
		grabacion = grabacion.trim();

	    Channel channel = sesion.openChannel("sftp");
		channel.connect();
		ChannelSftp c = (ChannelSftp)channel;
	    c.cd("/var/spool/asterisk/monitor");
	    
	    OutputStream salida = new BufferedOutputStream(new FileOutputStream("C:\\Users\\German\\"+grabacion));

	    MyProgressMonitor mon = new MyProgressMonitor();
	    c.get(grabacion, salida, mon);
	    
	    while (!mon.isEnd()) {
	    	Thread.sleep(500);
	    }
	    
	    c.disconnect();
	 }

}
