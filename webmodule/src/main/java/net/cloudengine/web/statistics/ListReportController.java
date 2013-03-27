package net.cloudengine.web.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.SimplePagingResult;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.reports.ReportMetadata;
import net.cloudengine.service.statistics.ReportException;
import net.cloudengine.service.statistics.ReportService;
import net.cloudengine.service.statistics.TokenService;

import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/report")
public class ListReportController {
	
	private Datastore<FileDescriptor, ObjectId> fileStore;
	private ReportService reportService;
	private TokenService tokenService;
	
	@Autowired
	public ListReportController(
			@Qualifier("fileStore") Datastore<FileDescriptor, ObjectId> fileStore,
			ReportService reportService,
			TokenService tokenService) {
		super();
		this.fileStore = fileStore;
		this.reportService = reportService;
		this.tokenService = tokenService;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/report/list/1/10");
		return mav;
	}
	
	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		List<FileDescriptor> reports = new ArrayList<FileDescriptor>(fileStore.createQuery().field("type").eq("rpt").list()); 
		mav.addObject("reports", new SimplePagingResult<FileDescriptor>(page, size, reports.size(), reports));
		mav.setViewName("/report/list");
		return mav;
	}
	
	@RequestMapping(value = "form/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		
		FileDescriptor fileDescriptor = fileStore.get(id);
		try {
			ReportMetadata rmd = reportService.getReportMetadata(fileDescriptor);
			mav.addObject("metadata", rmd);
			mav.addObject("report", fileDescriptor);
			mav.setViewName("/report/form");
			
		} catch (ReportException e) {
			mav.addObject("error", e);
			mav.setViewName("/report/error");
		}
		
		return mav;		

	}
	
	@RequestMapping(value = "generate/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> submitForm(@PathVariable("id") ObjectId id, WebRequest request, HttpServletResponse response) {
		
		Map<String,String[]> parametersMap = request.getParameterMap();
		Map<String,Object> params = new HashMap<String,Object>();
		for(String paramName : parametersMap.keySet()) {
			String[] va = parametersMap.get(paramName);
			if (va.length == 1) {
				params.put(paramName, va[0]);
			} else {
				params.put(paramName, va);
			}
		}
		
		try {
			FileDescriptor fileDescriptor = fileStore.get(id);
			byte[] report = reportService.executeReport(fileDescriptor, params);
			
			String reportfileName = 
					FilenameUtils.removeExtension(fileDescriptor.getFilename());
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Disposition", "attachment;filename="+reportfileName+".pdf");
//			responseHeaders.add("Set-Cookie", "fileDownload=true; path=/");
			
			return new ResponseEntity<byte[]>(report, responseHeaders, HttpStatus.OK);
		
		} catch (Exception e) {
			String msg = "<html>error</html>";
			return new ResponseEntity<byte[]>(msg.getBytes(), HttpStatus.OK);
		}	

	}
	
	
	@RequestMapping(value="progress")
	public @ResponseBody String checkDownloadProgress(@RequestParam String token) {
		return tokenService.check(token);
	}
	
	@RequestMapping(value="token")
	public @ResponseBody String getDownloadToken() {
		return tokenService.generate();
	}	
	
	

}
