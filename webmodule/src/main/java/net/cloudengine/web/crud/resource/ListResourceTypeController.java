package net.cloudengine.web.crud.resource;

import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.service.admin.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ListResourceTypeController {

	private ResourceService service;

	@Autowired
	public ListResourceTypeController(ResourceService service) {
		super();
		this.service = service;
	}
	
	@RequestMapping(value = "/resource/type/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/resource/type/list/1/10");
		return mav;
	}
	
	@RequestMapping(value = "/resource/type/list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("types", service.getAllTypes(page, size));
		mav.setViewName("/crud/resource/type/list");
		return mav;
	}
	
	@RequestMapping(value = "/resource/type/show/{id}", method = RequestMethod.GET)
	public ModelAndView showResourceType(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("type", service.getType(id));
		mav.setViewName("/crud/resource/type/show");
		return mav;
	}
	
	@RequestMapping(value = "/resource/type/image/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> showResourceTypeImage(@PathVariable("id") Long id) {
		ResourceType type = service.getType(id);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "image/png");
		return new ResponseEntity<byte[]>(type.getImage(), responseHeaders, HttpStatus.OK);
	}
	
}