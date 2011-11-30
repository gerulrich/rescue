package net.cloudengine;

import java.util.Locale;

import javax.annotation.PostConstruct;

import net.cloudengine.api.Query;
import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppListener {

	private static Logger logger = LoggerFactory.getLogger(AppListener.class);
	
	private MongoStore<User, ObjectId> ds;

	public AppListener(MongoStore<User, ObjectId> ds) {
		this.ds = ds;
	}

	@PostConstruct
	public void startup() {
		logger.debug("Verificando datos inciales");
		Query<User> query = ds.createQuery().field("username").eq("admin@admin.com");
		User user = query.get();
		if (user == null) {
			user = new User();
			user.setUsername("admin@admin.com");
			user.setPassword("d033e22ae348aeb5660fc2140aec35850c4da997");
			user.setDisplayName("Administrador");
			
			logger.debug("Creando el usuario admin@admin.com.");
			ds.save(user);
		} else {
			logger.debug("Ya existe el usuario admin@admin.com, se omite la creación.");
		}
		
		logger.debug("Utilizando locale = es_AR");
		Locale.setDefault(new Locale("ES", "AR"));
		
	}
}
