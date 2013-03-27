package net.cloudengine.web.crud.group;

import javax.validation.Valid;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.Group;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EditGroupController {

	private Datastore<Group,ObjectId> datastore;

	@Autowired
	public EditGroupController(
			@Qualifier("groupStore") Datastore<Group, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}
	
	@ModelAttribute("group")
	public Group getGroup(@PathVariable("id") ObjectId id) {
		return datastore.get(id);
	}
	
	@RequestMapping(value = "/group/edit/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView("/crud/group/form");
		return mav;
	}
	
	@RequestMapping(value = "/group/edit/{id}", method = RequestMethod.POST)
	public ModelAndView submitForm(
			@Valid @ModelAttribute("group") Group group, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.setViewName("/crud/group/form");
		} else {
			mav.setViewName("redirect:/group/list");
			datastore.update(group);
		}
		return mav;
	}

}
