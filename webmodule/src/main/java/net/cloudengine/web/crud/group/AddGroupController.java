package net.cloudengine.web.crud.group;

import javax.validation.Valid;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.Group;

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
public class AddGroupController {

	private Datastore<Group,ObjectId> datastore;

	@Autowired
	public AddGroupController(@Qualifier("groupStore") Datastore<Group, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
	}
	
	@ModelAttribute("group")
	public Group getGroup() {
		return new Group();
	}
	
	@RequestMapping(value = "/group/new", method = RequestMethod.GET)
	public ModelAndView setupForm() {
		return new ModelAndView("/crud/group/form");
	}
	
	@RequestMapping(value = "/group/new", method = RequestMethod.POST)
	public ModelAndView submitForm(@Valid @ModelAttribute("group") Group group, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/crud/group/form");
		} else {
			// TODO validar que no se repita el nombre del grupo
			datastore.save(group);
			mav.setViewName("redirect:/group/list/");
		}
		return mav;		
	}
	
}
