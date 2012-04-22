package net.cloudengine.client.service;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.apache.commons.io.IOUtils;

public class ConfigurationImpl implements Configuration {

	@Override
	public String getBaseUrl() {
		String baseUrl = null;
		try {
			BasicService basicService = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			String jnlpUrl = basicService.getCodeBase().toString();
			// obtengo las propiedades
			
			Properties properties = new Properties();
			loadProperties(properties, jnlpUrl.replaceAll("applications", "config"));
			baseUrl = properties.getProperty("url");
		} catch (UnavailableServiceException e) {
			baseUrl = "http://localhost:8080/webmodule/";
		}
		System.out.println("url de la aplicacion: "+baseUrl);
		return baseUrl;
	}
	
	private void loadProperties(Properties properties, String jnlpUrl) {
		InputStream is = null;
		try {
			Properties myProps = new Properties();
			is = new URL(jnlpUrl).openStream();
			myProps.load(is);
			
			String url = myProps.getProperty("url", "http://localhost:8080/webmodule/");
			properties.put("url", url);
			
		} catch (Exception e) {
			properties.put("url", "http://localhost:8080/webmodule/");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
}
