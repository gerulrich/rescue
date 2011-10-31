package net.rescueapp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppVersion {

	private static Logger logger = LoggerFactory.getLogger(AppVersion.class);

	public static void main(String args[]) {
		logger.info("Hello World, {}", "german");
		GroovyApp gapp = new GroovyApp();
		System.out.println(gapp.getMyProp());
	}

}