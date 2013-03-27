package net.cloudengine;

import java.util.Locale;

import javax.annotation.PostConstruct;

import net.cloudengine.web.MongoDBWrapper;
import net.cloudengine.xml.DataReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppListener {

	private static Logger logger = LoggerFactory.getLogger(AppListener.class);
	
	private MongoDBWrapper wrapper;
	
	public AppListener(MongoDBWrapper wrapper) {
		this.wrapper = wrapper;
	}

	@PostConstruct
	public void startup() {
		logger.debug("cargando datos inciales");
		new DataReader().loadAppData(wrapper.getFactory().getDb());
		
		logger.debug("Utilizando locale = es_AR");
		Locale.setDefault(new Locale("ES", "AR"));
		
	}
	
}
