package net.cloudengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import net.cloudengine.api.Query;
import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.config.AppProperty;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppListener {

	private static Logger logger = LoggerFactory.getLogger(AppListener.class);
	
	private MongoStore<User, ObjectId> ds;
	private MongoStore<AppProperty, ObjectId> dsProp;

	public AppListener(MongoStore<User, ObjectId> ds, MongoStore<AppProperty, ObjectId> dsProp) {
		this.ds = ds;
		this.dsProp = dsProp;
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
			user.setRoles("ADMIN");
			
			logger.debug("Creando el usuario admin@admin.com.");
			ds.save(user);
		} else {
			if (StringUtils.isEmpty(user.getRoles())) {
				user.setRoles("ADMIN");
				ds.update(user);
			}
			logger.debug("Ya existe el usuario admin@admin.com, se omite la creación.");
		}
		
		loadAppConfig();
		
		logger.debug("Utilizando locale = es_AR");
		Locale.setDefault(new Locale("ES", "AR"));
		
	}
	
	private void loadAppConfig() {
		List<AppProperty> properties = new ArrayList<AppProperty>();
		properties.add(new AppProperty("jnlp.url", "http://localhost:18080/client-deploy/applications/application1.jnlp"));
		properties.add(new AppProperty("map.google.street", "http://mt1.google.com/vt/lyrs=m@139&hl=es&x=${x}&y=${y}&z=${zoom}&s=Galil"));
		properties.add(new AppProperty("map.google.sat", "http://khm0.google.com.ar/kh/v=101&x=${x}&y=${y}&z=${zoom}&s=Galil"));
		properties.add(new AppProperty("map.osm", "http://b.tile.openstreetmap.org/${zoom}/${x}/${y}.png"));
		properties.add(new AppProperty("asterisk.hostname", "192.168.0.104", true));
		properties.add(new AppProperty("asterisk.manager.user", "manager", true));
		properties.add(new AppProperty("asterisk.manager.pass", "manager", true));
		properties.add(new AppProperty("asterisk.context", "from-internal", true));
		properties.add(new AppProperty("asterisk.enabled", "false", true));
		properties.add(new AppProperty("asterisk.rec.directory", "/var/spool/asterisk/monitor"));
		
		properties.add(new AppProperty("asterisk.ssh.user", "root"));
		properties.add(new AppProperty("asterisk.ssh.pass", "pandora"));
		
		
		
		for (AppProperty prop : properties) {
			Query<AppProperty> q = dsProp.createQuery().field("key").eq(prop.getKey());
			if (q.get() == null) {
				logger.debug("Guardando propiedad {} = {}", prop.getKey(), prop.getValue());
				dsProp.save(prop);
			}
		}
	}
}
