package net.cloudengine.web.crud.user;

import javax.annotation.Resource;
import javax.validation.Valid;

import net.cloudengine.api.Datastore;
import net.cloudengine.forms.NewUser;
import net.cloudengine.model.auth.User;
import net.cloudengine.util.Cipher;

import org.bson.types.ObjectId;
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
	
	private Datastore<User,ObjectId> datastore; 
	
	@Resource(name="userStore")
	public void setDatastore(Datastore<User, ObjectId> datastore) {
		this.datastore = datastore;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
	}
	
	@ModelAttribute("user")
	public NewUser getUser() {
		return new NewUser();
	}
	
	@RequestMapping(value = "/admin/user/new", method = RequestMethod.GET)
	public ModelAndView setupForm() {
		return new ModelAndView("/users/newform");
	}
	
	@RequestMapping(value = "/admin/user/new", method = RequestMethod.POST)
	public ModelAndView submitForm(@Valid @ModelAttribute("user") NewUser newUser, BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/users/newform");
		} else {
			User user = new User();
			user.setDisplayName(newUser.getDisplayName());
			user.setUsername(newUser.getEmail());
			user.setPassword(new Cipher().encrypt(newUser.getPassword()));
			user.setRoles(newUser.getRoles());
			datastore.save(user);
			mav.setViewName("redirect:/admin/user/show/" + user.getId());
		}
		return mav;		
		
	}
	
}
