package net.cloudengine.service.auth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.cloudengine.model.config.AppProperty;
import net.cloudengine.service.admin.ConfigurationService;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XMPPServiceImpl implements XMPPService {

	private static final int TIME_OUT = 3000;
	
	private ConfigurationService configuration;
	
	@Autowired
	public XMPPServiceImpl(ConfigurationService configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	public int addUser(String email, String password, String name) {
		return executeAction(email, password, name, "add");		
	}	

	@Override
	public int updateUser(String email, String password, String name) {
		return executeAction(email, password, name, "update");
	}
	
	@Override
	public int deleteUser(String email) {
		return executeAction(email, null, null, "delete");
	}
	
	private int executeAction(String email, String password, String name, String type) {
		if (isXMPPEnabled()) {
			String username = email.replaceAll("@.*", "");
			String host = configuration.getProperty("asterisk.hostname").getValue();
			String token = configuration.getProperty("openfire.token").getValue();
			
			try {
				String url = createRequest(type, username, password, name, email, host, token);
				String result = new String(executeRquest(url));
				parseResult(result);
			} catch (IOException e) {
				
			} catch (EncoderException e) {
				
			}
			
		}
		
		
		return 0;
	}
	
	private boolean isXMPPEnabled() {
		AppProperty enabled = configuration.getProperty("openfire.enabled");
		return enabled != null &&
			Boolean.TRUE.equals(Boolean.valueOf(enabled.getValue()));
	}
	
	/**
	 * Genera el string para el request de agregar usuario,
	 * actualizar usuario o borrar usuario.
	 * @param type tipo de request: add, update, delete.
	 * @param username nombre del usaurio (identificador)
	 * @param password contraseña del usaurio.
	 * @param name nombre del usuario (descripcion).
	 * @param email e-mail del usaurio.
	 * @param host ip o nombre del servidor
	 * @param token token de autenticacion.
	 * @return
	 */
	private String createRequest(
			String type, String username, 
			String password, String name, 
			String email, String host, String token) throws EncoderException {

		URLCodec codec = new URLCodec();
		String url;
		if ("delete".equals(type)) {
			url = "http://"+host+":9090/plugins/userService/userservice?"+
					"type=" + codec.encode(type) +
					"&secret=" + codec.encode(token) +
					"&username=" + codec.encode(username);
		} else {
			url = "http://"+host+":9090/plugins/userService/userservice?"+
					"type=" + codec.encode(type) +
					"&secret=" + codec.encode(token) +
					"&username=" + codec.encode(username) +
					"&password=" + codec.encode(password) +
					"&name=" + codec.encode(name) +
					"&email=" + codec.encode(email);
		}
		return url;
	}
	
	/**
	 * 
	 * IllegalArgumentException
	 * UserNotFoundException
	 * UserAlreadyExistsException
	 * RequestNotAuthorised
	 * UserServiceDisabled

	 * <error>RequestNotAuthorised</error>
	 * <result>ok</result>
	 * 
	 * 
	 * @param text
	 * @return
	 */
	private boolean parseResult(String text) {
		return true;
	}
	
	private byte[] executeRquest(String url) throws IOException {
		InputStream is = null;
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setConnectTimeout(TIME_OUT);
			con.setReadTimeout(TIME_OUT);
			is = con.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			IOUtils.copy(is, buffer);
			return buffer.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
