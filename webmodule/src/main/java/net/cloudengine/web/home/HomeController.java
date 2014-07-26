package net.cloudengine.web.home;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.chart.ChartInfo;

import org.bson.types.ObjectId;
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

	private Repository<ChartInfo, ObjectId> chartInfoRepository;
	
	@Autowired
	public HomeController(RepositoryLocator repositoryLocator) {
		chartInfoRepository = repositoryLocator.getRepository(ChartInfo.class);
	}
	
	@RequestMapping("home")
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("charts", chartInfoRepository.getAll());
		mav.addObject("env", System.getenv());
		mav.setViewName("home");
		return mav;
	}
}
