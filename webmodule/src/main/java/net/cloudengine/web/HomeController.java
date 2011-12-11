package net.cloudengine.web;

import javax.servlet.http.HttpServletRequest;

import net.cloudengine.service.admin.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * Controller para la página de inicio.
 *
 * @author German Ulrich
 *
 */
@Controller
public class HomeController {
	
	private ConfigurationService configService;
	
	@Autowired 
	public HomeController(ConfigurationService configService) {
		super();
		this.configService = configService;
	}



	@RequestMapping("home")
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("options", configService.getAvailableServices());
		mav.setViewName("home");
		return mav;
	}
}
