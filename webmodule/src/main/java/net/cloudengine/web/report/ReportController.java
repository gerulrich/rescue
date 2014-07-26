package net.cloudengine.web.report;

import net.cloudengine.model.report.Report;
import net.cloudengine.service.ReportService;

import org.bson.types.ObjectId;
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
@RequestMapping("/report")
public class ReportController {
	
	
	private ReportService reportService;
	
	@Autowired
	public ReportController(ReportService reportService) {
		super();
		this.reportService = reportService;
	}

	@RequestMapping(value = "show/{id}", method = RequestMethod.GET)
	public ModelAndView resultPage(@PathVariable ObjectId id) {
		// TODO validar owner.
		Report report = reportService.getReport(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("report", report);
		mav.setViewName("/report/result");
		return mav;
	}
	
	@RequestMapping(value = "download/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]>  download(@PathVariable ObjectId id) {
		Report report = reportService.getReport(id);
		if (report != null && report.getData() != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "image/png");
			responseHeaders.set("Content-Disposition", "attachment;filename=report.pdf");
			return new ResponseEntity<byte[]>(report.getData(), responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listMyReports() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/report/list/1/10");
		return mav;
	}

	@RequestMapping(value = "/list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView listDocuments(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("reports", reportService.getReportsForCurrentUser(page, size));
		mav.setViewName("/report/documents");
		return mav;
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") ObjectId pk) {
		reportService.deleteReports(pk);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/report/list/1/10");		
		return mav;
	}
}
