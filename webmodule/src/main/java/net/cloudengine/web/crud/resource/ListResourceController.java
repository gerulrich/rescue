package net.cloudengine.web.crud.resource;

import net.cloudengine.service.admin.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ListResourceController {

	private ResourceService service;

	@Autowired
	public ListResourceController(ResourceService service) {
		super();
		this.service = service;
	}
	
	@RequestMapping(value = "/resource/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/resource/list/1/10");
		return mav;
	}
	
	@RequestMapping(value = "/resource/list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("resources", service.getAll(page, size));
		mav.setViewName("/crud/resource/list");
		return mav;
	}
	
	@RequestMapping(value = "/resource/show/{id}", method = RequestMethod.GET)
	public ModelAndView showResource(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("resource", service.get(id));
		mav.setViewName("/crud/resource/show");
		return mav;
	}
	
}