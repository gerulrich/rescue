package net.cloudengine.web.admin;

import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.config.AppProperty;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller para la edición de Properties de configuración de la aplicación.
 * @author German ulrich
 *
 */
@Controller
public class PropertyController {
	
	
	private MongoStore<AppProperty, ObjectId> datastore;
	
	@Autowired
	public PropertyController(@Qualifier("propertyStore") MongoStore<AppProperty, ObjectId> datastore) {
		this.datastore = datastore;
	}
	
	@RequestMapping(value = "/admin/properties", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/admin/properties/1/25");
		return mav;
	}
	
	@RequestMapping(value = "/admin/properties/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("properties", datastore.list(page, size));
		mav.setViewName("/properties/list");
		return mav;
	}

}
