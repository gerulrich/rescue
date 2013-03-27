package net.cloudengine.web.crud.role;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.Role;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteRolController {
	
	private Datastore<Role,ObjectId> datastore;

	@Autowired
	public DeleteRolController(@Qualifier("roleStore") Datastore<Role, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}
	
	@RequestMapping(value = "/role/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteUser(@PathVariable("id") ObjectId id) {
		datastore.delete(id);
		return new ModelAndView("redirect:/roles/list");
	}

}
