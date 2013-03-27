package net.cloudengine.web.crud.role;

import javax.validation.Valid;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.Role;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddRoleController {

	private Datastore<Role,ObjectId> datastore;

	@Autowired
	public AddRoleController(@Qualifier("roleStore") Datastore<Role, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "permissions");
	}
	
	@ModelAttribute("role")
	public Role getRole() {
		return new Role();
	}
	
	@RequestMapping(value = "/role/new", method = RequestMethod.GET)
	public ModelAndView setupForm() {
		return new ModelAndView("/crud/role/form");
	}
	
	@RequestMapping(value = "/role/new", method = RequestMethod.POST)
	public ModelAndView submitForm(@Valid @ModelAttribute("role") Role role, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/crud/role/form");
		} else {
			// TODO validar que no se repita el nombre del rol
			datastore.save(role);
			mav.setViewName("redirect:/roles/list/");
		}
		return mav;		
		
	}
	
}
