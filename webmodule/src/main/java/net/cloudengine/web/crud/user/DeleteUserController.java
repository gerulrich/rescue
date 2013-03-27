package net.cloudengine.web.crud.user;

import net.cloudengine.service.auth.UserService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteUserController {

	private UserService service;
	
	@Autowired
	public DeleteUserController(UserService service) {
		super();
		this.service = service;
	}

	@RequestMapping(value = "/delete/user/{id}", method = RequestMethod.GET)
	public ModelAndView deleteUser(@PathVariable("id") ObjectId id) {
		service.deleteUser(id);
		return new ModelAndView("redirect:/user/list");
	}

}
