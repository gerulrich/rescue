package net.cloudengine;

import java.util.Locale;

import javax.annotation.PostConstruct;

import net.cloudengine.api.mongo.MongoDBWrapper;
import net.cloudengine.reports.ReportExecution;
import net.cloudengine.xml.DataReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Morphia;

public class AppListener {

	private static Logger logger = LoggerFactory.getLogger(AppListener.class);
	
	private MongoDBWrapper wrapper;
	private Morphia morphia;
	
	public AppListener(MongoDBWrapper wrapper, Morphia morphia) {
		this.wrapper = wrapper;
		this.morphia = morphia;
	}

	@PostConstruct
	public void startup() {
		logger.debug("cargando datos inciales");
		new DataReader().loadAppData(wrapper.getFactory().getDb(), "/net/cloudengine/xml/data.xml");
		
		logger.debug("Utilizando locale = es_AR");
		Locale.setDefault(new Locale("ES", "AR"));
		
		morphia.map(ReportExecution.class)
		.createDatastore(wrapper.getFactory().getDb().getMongo(), wrapper.getFactory().getDb().getName())
		.ensureIndexes();
		
	}
	
}
