package net.cloudengine;

import java.util.Locale;

import javax.annotation.PostConstruct;

import net.cloudengine.xml.DataReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

public class AppListener {

	private static Logger logger = LoggerFactory.getLogger(AppListener.class);
	
	private MongoTemplate mongoTemplate;
	
	public AppListener(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@PostConstruct
	public void startup() {
		logger.debug("cargando datos inciales");
		new DataReader().loadAppData(mongoTemplate.getDb(), "/net/cloudengine/xml/data.xml");
		
		logger.debug("Utilizando locale = es_AR");
		Locale.setDefault(new Locale("ES", "AR"));

	}
	
}
