package net.cloudengine.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import net.cloudengine.model.auth.User;
import net.cloudengine.util.Cipher;
import net.cloudengine.web.servlet.CurrentUser;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AvatarController {
	
	/**
	 * Genera la imagen del avatar para el usuario actual y la envia en la respuesta http.
	 * @param user usuario actual
	 * @param resp 
	 */
	@RequestMapping(value="/avatar", method=RequestMethod.GET)
	public void avatar(@CurrentUser User user, HttpServletResponse resp) {
	
		URL url = getAvatarUrl(user);
		
		OutputStream out = null;
		InputStream is = null;
		
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
			
			is = con.getInputStream();
			out = resp.getOutputStream();
			
			resp.setContentType(con.getContentType());
			
			IOUtils.copy(is, out);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(out);
		}
	}
	
	private URL getAvatarUrl(User user) {
		try {
			String md5 = new Cipher().encrypt(user.getUsername().trim().toLowerCase(), "MD5");
			String url = "http://www.gravatar.com/avatar/"+md5+"?s=64";
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}	
}
