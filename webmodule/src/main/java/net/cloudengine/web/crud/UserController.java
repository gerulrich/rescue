package net.cloudengine.web.crud;

import javax.annotation.Resource;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.Query;
import net.cloudengine.model.auth.User;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController extends CrudController {
	

	protected static final String URL_LIST_REDIRECT = "redirect:/admin/users/1/10";
	
	Datastore<User,ObjectId> ds;
	
	@Resource(name="userStore")
	public void setDatastore(Datastore<User, ObjectId> datastore) {
		this.ds= datastore;
	}


	@Override
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(URL_LIST_REDIRECT);
		return mav;
	}
	
	
	@Override
	@RequestMapping(value = "/admin/users/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
//		Datastore<User,ObjectId> ds = new ServiceLookup().createDatastore(User.class, ObjectId.class);
		ModelAndView mav = new ModelAndView();
		mav.addObject("users", ds.list(page, size));
		mav.setViewName("/users/list");
		return mav;
	}
	
//	@Override
	@RequestMapping(value = "/admin/user/show/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") ObjectId id) {
		return new ModelAndView("/users/show", "user", getUser(id));
	}
	

	private User getUser(ObjectId id) {
//		Datastore<User,ObjectId> ds = new ServiceLookup().createDatastore(User.class, ObjectId.class);
		Query<User> query = ds.createQuery().field(ID).eq(id);
		User user = query.get();
		return user;
	}
	
}