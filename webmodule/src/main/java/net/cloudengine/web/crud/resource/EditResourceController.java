package net.cloudengine.web.crud.resource;

import java.util.Collection;

import javax.validation.Valid;

import net.cloudengine.model.resource.Resource;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.service.admin.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
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
public class EditResourceController {
	
	private ResourceService service;
	
	@Autowired
	public EditResourceController(ResourceService service) {
		super();
		this.service = service;
	}	

	@InitBinder 
	public void initBinder(WebDataBinder b) {
		b.setDisallowedFields("id");
		b.registerCustomEditor(ResourceType.class, new ResourceTypeEditor(service));
	}

	@ModelAttribute("resource")
	public Resource getResource(@PathVariable("id") Long id) {
		return service.get(id);
	}
	
	@ModelAttribute("types")
	public Collection<ResourceType> getAllresourceTypes() {
		return service.getAllTypes(1, 1).getCompleteList();
	}
	
	@RequestMapping(value = "/resource/edit/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") Long id) {
		return new ModelAndView("/crud/resource/form");
	}
	
	@RequestMapping(value = "/resource/edit/{id}", method = RequestMethod.POST)
	public ModelAndView submitForm(@Valid @ModelAttribute("resource") Resource resource, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.setViewName("/crud/resource/form");
		} else {
			service.update(resource);
			mav.setViewName("redirect:/resource/show/"+resource.getId());
		}
		return mav;
	}	
}
