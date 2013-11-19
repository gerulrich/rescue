package net.cloudengine.web.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cloudengine.api.Datastore;
import net.cloudengine.api.SimplePagingResult;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.reports.ReportExecution;
import net.cloudengine.reports.ReportMetadata;
import net.cloudengine.service.statistics.ReportException;
import net.cloudengine.service.statistics.ReportService;
import net.cloudengine.service.web.CurrentUser;

import org.apache.commons.lang.exception.ExceptionUtils;
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
public class ReportController {
	
	private Datastore<FileDescriptor, ObjectId> fileStore;
	private ReportService reportService;
	private Datastore<ReportExecution, ObjectId> ds;	
	
	@Autowired
	public ReportController(
			@Qualifier("fileStore") Datastore<FileDescriptor, ObjectId> fileStore,
			@Qualifier("reportExecution") Datastore<ReportExecution, ObjectId> ds,
			ReportService reportService) {
		super();
		this.ds = ds;
		this.fileStore = fileStore;
		this.reportService = reportService;
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
			mav.addObject("datasource", rmd.getDatasource());
			mav.addObject("report", fileDescriptor);
			mav.setViewName("/report/form");

		} catch (ReportException e) {
			mav.addObject("error", e);
			mav.setViewName("/report/error");
		}

		return mav;

	}
	
	@RequestMapping(value = "generate/{id}", method = RequestMethod.GET)
	public @ResponseBody String submitForm(@PathVariable("id") ObjectId id, WebRequest request) {
		
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
		
		String token = null; 
		if (parametersMap.get("token")!=null) {
			token = parametersMap.get("token")[0];
		}
		
		ReportExecution exec = ds.get(new ObjectId(token));
		
		try {
			FileDescriptor fileDescriptor = fileStore.get(id);
			byte[] report = reportService.executeReport(fileDescriptor, params);
			exec.setReport(report);
			exec.setStatus("OK");
			exec.setFileName(fileDescriptor.getFilename());
			return "<html>OK</html>";
		
		} catch (Exception e) {
			
			exec.setStatus("ERROR");
			exec.setError(ExceptionUtils.getStackTrace(e));
			ds.update(exec);
			return "<html>ERROR</html>";
		} finally {
			
			ds.update(exec);
		}
	}
	
	@RequestMapping(value = "error", method = RequestMethod.GET)
	public ModelAndView errorPage(@RequestParam("token") String token) {
		ReportExecution exec = ds.get(new ObjectId(token));
		ModelAndView mav = new ModelAndView();
		mav.addObject("error", exec.getError());
		mav.setViewName("/report/error");
		return mav;
	}
	
	@RequestMapping(value = "download/{id}", method = RequestMethod.GET)
	public ModelAndView downloadPage(@PathVariable ObjectId id) {
		ReportExecution exec = ds.get(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("report", exec);
		mav.setViewName("/report/download");
		return mav;
	}
	
	@RequestMapping(value = "downloada/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable ObjectId id) {
		ReportExecution exec = ds.get(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("report", exec);
		mav.setViewName("/report/download");
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/pdf");
		return new ResponseEntity<byte[]>(exec.getReport(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="result")
	public @ResponseBody String checkResult(@RequestParam("token") String token) {
		ReportExecution exec = ds.get(new ObjectId(token));
		return exec.getStatus();
	}
	
	@RequestMapping(value="progress")
	public @ResponseBody String checkDownloadProgress(@RequestParam("token") ObjectId id) {
		ReportExecution exec = ds.get(id);
		return exec.getStatus();
	}
	
	@RequestMapping(value="token")
	public @ResponseBody String getDownloadToken(@CurrentUser User user) {
		ReportExecution exec = new ReportExecution();
		exec.setDate(new Date());
		exec.setUsername(user.getUsername());
		ds.save(exec);
		return exec.getId().toString();
	}

}
