package net.cloudengine.web.admin;

import net.cloudengine.api.mongo.MongoStore;
import net.cloudengine.model.config.AppProperty;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class EditPropertyController {
	
	
	private MongoStore<AppProperty, ObjectId> datastore;
	
	@Autowired
	public EditPropertyController(@Qualifier("propertyStore") MongoStore<AppProperty, ObjectId> datastore) {
		this.datastore = datastore;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "key");
	}
	
	@ModelAttribute("property")
	public AppProperty geProperty(@PathVariable("id") ObjectId id) {
		return datastore.get(id);
	}
	
	@RequestMapping(value = "/admin/property/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		return new ModelAndView("/properties/editform");
	}
	
	@RequestMapping(value = "/admin/property/{id}", method = RequestMethod.POST)
	public ModelAndView submit(@PathVariable("id") ObjectId id, AppProperty property) {
		AppProperty p = datastore.get(id); // FIXME
		p.setValue(property.getValue());
		datastore.update(p);
		return new ModelAndView("redirect:/admin/properties/");
	}

}
