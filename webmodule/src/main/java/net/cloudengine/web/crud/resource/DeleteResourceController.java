package net.cloudengine.web.crud.resource;

import net.cloudengine.service.admin.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteResourceController {
	
	private ResourceService service;

	@Autowired
	public DeleteResourceController(ResourceService service) {
		super();
		this.service = service;
	}
	
	@RequestMapping(value = "/resource/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id) {
		service.delete(id);
		return new ModelAndView("redirect:/resource/list");
	}

}
