package net.cloudengine.web;
import java.util.Collection;

import net.cloudengine.service.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ConfigController {

	
	@Autowired ConfigurationService configService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/config", method=RequestMethod.GET)
	public String home(Model model) {
		Collection<String> services = configService.getAvailableServices();
		model.addAttribute("services", services);
		return "services/ppp";
	}
	
}