package net.cloudengine.web.crud.resource;

import java.util.Collection;

import javax.validation.Valid;

import net.cloudengine.api.BlobStore;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.service.admin.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddResourceTypeController {

	private ResourceService service;
	private BlobStore blobStore;
	
	@Autowired
	public AddResourceTypeController(ResourceService service, BlobStore blobStore) {
		super();
		this.service = service;
		this.blobStore = blobStore;
	}
	
	@InitBinder 
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(byte[].class, new ImageEditor(service,blobStore));
	}
	
	@ModelAttribute("type")
	public ResourceType getResourceType() {
		return new ResourceType();
	}
	
	@ModelAttribute("files")
	public Collection<FileDescriptor> getImages() {
		return service.getAllImages();
	}
	
	@RequestMapping(value = "/resource/type/new", method = RequestMethod.GET)
	public ModelAndView setupForm() {
		ModelAndView mav = new ModelAndView("/crud/resource/type/form");
		return mav;
	}
	
	@RequestMapping(value = "/resource/type/new", method = RequestMethod.POST)
	public ModelAndView submitForm(@Valid @ModelAttribute("type") ResourceType type, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		if (result.hasErrors()) {
			mav.setViewName("/crud/resource/type/form");
		} else {
			service.save(type);
			mav.setViewName("redirect:/resource/type/list");
		}
		return mav;
	}
}
