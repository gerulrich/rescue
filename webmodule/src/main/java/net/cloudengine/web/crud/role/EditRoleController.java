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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/role")
public class EditRoleController {

	private Datastore<Role,ObjectId> datastore;

	@Autowired
	public EditRoleController(
			@Qualifier("roleStore") Datastore<Role, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "username","password");
	}
	
	@ModelAttribute("role")
	public Role getRole(@PathVariable("id") ObjectId id) {
		return datastore.get(id);
	}
	
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		return new ModelAndView("/crud/role/form");
	}
	
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public ModelAndView submitForm(
			@PathVariable("id") ObjectId id,  
			@Valid @ModelAttribute("role") Role role, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();

		if (result.hasErrors()) {
			mav.setViewName("/crud/role/form");
		} else {
			datastore.update(role);
			mav.setViewName("redirect:/role/show/" + id);
		}
		
		
		return mav;
	}	
	
}
