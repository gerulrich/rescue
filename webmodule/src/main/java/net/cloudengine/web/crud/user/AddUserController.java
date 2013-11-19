package net.cloudengine.web.crud.user;

import javax.validation.Valid;

import net.cloudengine.forms.PasswordForm;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddUserController {
	
	private UserService service;
	
	@Autowired
	public AddUserController(UserService service) {
		super();
		this.service = service;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
		binder.registerCustomEditor(Group.class, new GroupEditor());
	}
	
	@ModelAttribute("user")
	public User getUser() {
		return new User();
	}
	
	@ModelAttribute("passwordForm")
	public PasswordForm getPasswordForm() {
		return new PasswordForm();
	}
	
	@RequestMapping(value = "/user/new", method = RequestMethod.GET)
	public ModelAndView setupForm() {
		ModelAndView mav = new ModelAndView("/crud/user/form"); 
		mav.addObject("groups", this.service.getGroups());
		return mav;
	}
	
	@RequestMapping(value = "/user/new", method = RequestMethod.POST)
	public ModelAndView submitForm(
		@Valid @ModelAttribute("user") User user,
		BindingResult result1,
		@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm,
		BindingResult result2) {

		ModelAndView mav = new ModelAndView();

		if (result1.hasErrors() || result2.hasErrors()) {
			mav.setViewName("/crud/user/form");
		} else {
			ObjectId id = service.addUser(user, passwordForm.getPassword());
			mav.setViewName("redirect:/user/show/" + id);
		}
		return mav;		
	}
}
