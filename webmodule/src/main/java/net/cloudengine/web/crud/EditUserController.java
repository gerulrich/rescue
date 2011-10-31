package net.cloudengine.web.crud;

import javax.annotation.Resource;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
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
	
	private static final String URI = "/admin/user/{id}";
	private Datastore<User,ObjectId> datastore; 
	
	@Resource(name="userStore")
	public void setDatastore(Datastore<User, ObjectId> datastore) {
		this.datastore = datastore;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "username","password");
	}
	
	@ModelAttribute("user")
	public User getUser(@PathVariable("id") ObjectId id) {
		Query<User> query = datastore.createQuery().field("id").eq(id);
		return query.get();
	}
	
	@RequestMapping(value = URI, method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		return new ModelAndView("/users/form");
	}
	
	@RequestMapping(value = URI, method = RequestMethod.POST)
	public ModelAndView submit(@ModelAttribute("user") User user, BindingResult result) {
		datastore.update(user);
		return new ModelAndView("redirect:/admin/user/show/" + user.getId());
	}

}
