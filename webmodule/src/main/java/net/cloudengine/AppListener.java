package net.cloudengine;

import javax.annotation.PostConstruct;

import net.cloudengine.api.Query;
import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;

public class AppListener {

	private MongoStore<User, ObjectId> ds;

	public AppListener(MongoStore<User, ObjectId> ds) {
		this.ds = ds;
	}

	@PostConstruct
	public void startup() {
		Query<User> query = ds.createQuery().field("username").eq("admin@admin.com");
		User user = query.get();
		if (user == null) {
			user = new User();
			user.setUsername("admin@admin.com");
			user.setPassword("d033e22ae348aeb5660fc2140aec35850c4da997");
			user.setDisplayName("Administrador");
			ds.save(user);
		}
	}

}
