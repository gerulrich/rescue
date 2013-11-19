package net.cloudengine.web.home;

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
	
	@RequestMapping("home")
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");
		return mav;
	}
}
