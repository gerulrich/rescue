package net.cloudengine.web.report;

import java.util.Map;

import net.cloudengine.model.auth.User;
import net.cloudengine.model.report.Report;
import net.cloudengine.model.report.ReportMetadata;
import net.cloudengine.service.ReportService;
import net.cloudengine.service.web.CurrentUser;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/report/metadata")
public class ReportMetadataController {

	private ReportService reportService;
	
	@Autowired
	public ReportMetadataController(ReportService reportService) {
		super();
		this.reportService = reportService;
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/report/metadata/list/1/10");
		return mav;
	}

	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("reports", reportService.getReportsMetadata(page, size));
		mav.setViewName("/report/metadata/list");
		return mav;
	}
	
	@RequestMapping(value = "form/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable ObjectId id, @CurrentUser User user) {
		ModelAndView mav = new ModelAndView();
		ReportMetadata reportMetadata = reportService.getReportMetadata(id); 
		mav.addObject("report", reportMetadata);
		mav.addObject("params", reportService.getValidParametersForCurrentUser(reportMetadata));
		mav.setViewName("/report/metadata/form");
		return mav;
	}
	
	@RequestMapping(value = "generate/{id}", method = RequestMethod.POST)
	public ModelAndView submitForm(@PathVariable ObjectId id, WebRequest request) {
		Map<String,String[]> requestParameters = request.getParameterMap();
		Map<String,String> parameters = Maps.newHashMap();
		for (Map.Entry<String, String[]> entry : requestParameters.entrySet()) {
			if (entry.getValue() != null && entry.getValue().length > 0) {
				parameters.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		Report report = reportService.createReport(id, parameters);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/report/show/"+report.getId());
		return mav;
	}	
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") ObjectId pk) {
		reportService.deleteReportsMetadata(pk);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/report/metadata/list/1/10");		
		return mav;
	}	
	
	
}
