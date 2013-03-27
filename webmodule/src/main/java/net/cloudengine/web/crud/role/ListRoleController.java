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
@RequestMapping("/role")
public class ListRoleController {
	
	private Datastore<Role,ObjectId> datastore;

	@Autowired
	public ListRoleController(@Qualifier("roleStore") Datastore<Role, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/role/list/1/10");
		return mav;
	}
	
	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("roles", datastore.list(page, size));
		mav.setViewName("/crud/role/list");
		return mav;
	}
	
	@RequestMapping(value = "show/{id}", method = RequestMethod.GET)
	public ModelAndView showRole(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("role", datastore.get(id));
		mav.setViewName("/crud/role/show");
		return mav;
	}
	
	

}
