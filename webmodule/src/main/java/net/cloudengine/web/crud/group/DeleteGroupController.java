package net.cloudengine.web.crud.group;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.Group;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteGroupController {
	
	private Datastore<Group,ObjectId> datastore;

	@Autowired
	public DeleteGroupController(@Qualifier("groupStore") Datastore<Group, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}
	
	@RequestMapping(value = "/group/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteUser(@PathVariable("id") ObjectId id) {
		datastore.delete(id);
		return new ModelAndView("redirect:/group/list");
	}

}
