package net.cloudengine.web.crud.user;

import javax.validation.Valid;

import net.cloudengine.api.Datastore;
import net.cloudengine.forms.PasswordForm;
import net.cloudengine.model.auth.User;
import net.cloudengine.util.Cipher;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChangePasswordController {
	
	
	private Datastore<User, ObjectId> datastore;

	@Autowired
	public ChangePasswordController(@Qualifier("userStore") Datastore<User, ObjectId> datastore) {
		super();
		this.datastore = datastore;
	}

	@ModelAttribute("user")
	public User getUser(@PathVariable("id") ObjectId id) {
		return datastore.get(id);
	}
	
	@ModelAttribute("passwordForm")
	public PasswordForm getPasswordForm() {
		return new PasswordForm();
	}

	@RequestMapping(value = "/user/password/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id, 
			@RequestParam(value="result", required=false, defaultValue="") String result) {
		ModelAndView mav = new ModelAndView("/crud/user/passwordform");
		mav.addObject("result", result);
		return mav;
	}
	
	@RequestMapping(value = "/user/password/{id}", method = RequestMethod.POST)
	public ModelAndView submit(
			@PathVariable("id") ObjectId id,
			@Valid @ModelAttribute("passwordForm") PasswordForm form,
			BindingResult result) {
		
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.setViewName("/crud/user/passwordForm");
			return mav;
		} else {
			User user = datastore.get(id);
			user.setPassword(new Cipher().encrypt(form.getPassword()));
			datastore.update(user);
			mav.setViewName("redirect:/user/password/"+user.getId().toString()+"?result=0");
			return mav;
		}
	}

}
