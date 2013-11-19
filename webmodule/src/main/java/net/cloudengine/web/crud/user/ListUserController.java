package net.cloudengine.web.crud.user;

import net.cloudengine.api.mongo.dao.UserDao;
import net.cloudengine.model.config.AppProperty;
import net.cloudengine.service.admin.ConfigurationService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ListUserController {
	
	protected static final String URL_LIST_REDIRECT = "redirect:/user/list/1/10";
	private UserDao userDao;
	private ConfigurationService service;
	
	@Autowired
	public ListUserController(UserDao userDao, ConfigurationService service) {
		super();
		this.service = service;
		this.userDao = userDao;
	}

	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(URL_LIST_REDIRECT);
		return mav;
	}
	
	@RequestMapping(value = "/user/list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size, 
			@RequestParam(value="q", required=false, defaultValue="") String queryFilter) {
		AppProperty xmppEnabled = service.getProperty("openfire.enabled");
		AppProperty xmppDomain = service.getProperty("openfire.domain");
		ModelAndView mav = new ModelAndView();
		mav.addObject("q", queryFilter);
		mav.addObject("users", userDao.list(page, size));
		mav.addObject("openfire_enabled", Boolean.valueOf(xmppEnabled.getValue()));
		mav.addObject("openfire_domain", xmppDomain.getValue());
		mav.setViewName("/crud/user/list");
		return mav;
	}
	
	@RequestMapping(value = "/user/show/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") ObjectId id) {
		return new ModelAndView("/crud/user/show", "user", userDao.get(id));
	}
	
}