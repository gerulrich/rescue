package net.cloudengine.web.crud.user;

import javax.validation.Valid;

import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;
import net.cloudengine.service.auth.UserService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EditUserController {
	
	private static final String URI = "/user/{id}";
	private UserService service; 
	
	@Autowired
	public EditUserController(UserService service) {
		super();
		this.service = service;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "username","password");
		binder.registerCustomEditor(Group.class, new GroupEditor());
	}
	
	@ModelAttribute("user")
	public User getUser(@PathVariable("id") ObjectId id) {
		return service.get(id);
	}
	
	@RequestMapping(value = URI, method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView("/crud/user/form"); 
		mav.addObject("groups", this.service.getGroups());
		return mav;
	}
	
	@RequestMapping(value = URI, method = RequestMethod.POST)
	public ModelAndView submit(@Valid @ModelAttribute("user") User user, BindingResult result) {
		service.updateUser(user);
		return new ModelAndView("redirect:/user/show/" + user.getId());
	}

}
