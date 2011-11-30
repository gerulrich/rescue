package net.cloudengine.web.crud.user;

import javax.annotation.Resource;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	

	protected static final String URL_LIST_REDIRECT = "redirect:/admin/users/1/10";
	
	Datastore<User,ObjectId> datastore;
	
	@Resource(name="userStore")
	public void setDatastore(Datastore<User, ObjectId> datastore) {
		this.datastore= datastore;
	}


	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(URL_LIST_REDIRECT);
		return mav;
	}
	
	@RequestMapping(value = "/admin/users/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", datastore.list(page, size));
		mav.setViewName("/users/list");
		return mav;
	}
	
	@RequestMapping(value = "/admin/user/show/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") ObjectId id) {
		return new ModelAndView("/users/show", "user", datastore.get(id));
	}
	
}