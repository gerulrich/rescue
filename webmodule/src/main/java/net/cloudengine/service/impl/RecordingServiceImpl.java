package net.cloudengine.service.impl;

import java.io.OutputStream;
import java.util.Properties;

import net.cloudengine.service.ConfigurationService;
import net.cloudengine.service.RecordingService;
import net.cloudengine.validation.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service
public class RecordingServiceImpl implements RecordingService {

	private static final String HOSTNAME = "asterisk.hostname";
	private static final String SSH_PASS = "asterisk.ssh.pass";
	private static final String SSH_USER = "asterisk.ssh.user";
	private ConfigurationService configuration;
	
	@Autowired
	public RecordingServiceImpl(ConfigurationService configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public boolean getAudioRecord(String filename, OutputStream out) {
		Assert.notNull(filename, "El nombre de archivo no puede ser nulo");
		Assert.notEmpty(filename, "El nombre de archivo no puede ser vacio");
		
		String username = configuration.getProperty(SSH_USER).getValue();
		String password = configuration.getProperty(SSH_PASS).getValue();
		String hostname = configuration.getProperty(HOSTNAME).getValue();
		String directory = configuration.getProperty("asterisk.rec.directory").getValue();
		int port = 22;

		
		ChannelSftp sftpChannel = null;
		Session sesion = null;
		
		try {
		
			JSch jsch = new JSch();
			sesion = jsch.getSession(username, hostname, port);
			sesion.setPassword(password);

			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			sesion.setConfig(config);

			sesion.connect();
			
		    Channel channel = sesion.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp)channel;
		    sftpChannel.cd(directory);
		    
		    ProgressMonitor mon = new ProgressMonitor();
		    sftpChannel.get(filename, out, mon);
		    
		    while (!mon.isEnd()) {
		    	Thread.sleep(50);
		    }
		    
		    return true;
			
		} catch (InterruptedException e) {
			// no se pudo obener el arhcivo.
			e.printStackTrace();
		} catch (JSchException e) {
			// no se pudo obtener el archivo.
			e.printStackTrace();
		} catch (SftpException e) {
			// no se pudo obener el arhcivo.
			e.printStackTrace();
		} finally {
			if (sftpChannel != null) {
				sftpChannel.disconnect();
			}
			if (sesion != null) {
				sesion.disconnect();
			}
		}
		return false;
	}

}
