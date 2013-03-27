package net.cloudengine.web.crud.property;

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
public class ListPropertyController {
	
	
	private MongoStore<AppProperty, ObjectId> datastore;
	
	@Autowired
	public ListPropertyController(@Qualifier("propertyStore") MongoStore<AppProperty, ObjectId> datastore) {
		this.datastore = datastore;
	}
	
	@RequestMapping(value = "/config/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/config/list/1/25");
		return mav;
	}
	
	@RequestMapping(value = "/config/list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("properties", datastore.list(page, size));
		mav.setViewName("/crud/properties/list");
		return mav;
	}

}
