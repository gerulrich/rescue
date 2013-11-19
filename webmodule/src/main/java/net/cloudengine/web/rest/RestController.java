package net.cloudengine.web.rest;

import static net.cloudengine.rest.model.resource.Response.INVALID_CREDENTIALS;
import static net.cloudengine.rest.model.resource.Response.OK_RESPONSE;
import static net.cloudengine.rest.model.resource.Response.SUCCESSFUL;
import static net.cloudengine.rest.model.resource.Response.UNAUTHRORIZED;

import java.util.List;

import javax.validation.Valid;

import net.cloudengine.forms.auth.LoginForm;
import net.cloudengine.model.auth.User;
import net.cloudengine.reports.ReportExecution;
import net.cloudengine.rest.model.resource.ReportModel;
import net.cloudengine.rest.model.resource.Response;
import net.cloudengine.rpc.mappers.DTOMapper;
import net.cloudengine.rpc.mappers.MappersRegistry;
import net.cloudengine.service.auth.AuthenticationService;
import net.cloudengine.service.gcm.GCMMessage;
import net.cloudengine.service.gcm.GCMService;
import net.cloudengine.service.gcm.GCMSimpleMessage;
import net.cloudengine.service.statistics.ReportService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {

	private GCMService gcmService;
	private AuthenticationService authService;
	private ReportService reportService;
	private MappersRegistry mappersRegistry;
	
	@Autowired
	public RestController(AuthenticationService authService, GCMService gcmService, ReportService reportService, 
			MappersRegistry mappersRegistry) {
		super();
		this.gcmService = gcmService;
		this.authService = authService;
		this.reportService = reportService;
		this.mappersRegistry = mappersRegistry;
	}

	@RequestMapping(value = "rest/authenticate", method = RequestMethod.POST)
    public @ResponseBody Response<String> authenticate(@Valid @ModelAttribute LoginForm form, BindingResult result) {
		if (result.hasErrors()) {
			return unauthorizedResponse();
		}
		String token = authService.createToken(form.getUsername(), form.getPassword());
		if (token != null) {
			return buildResponse(token);
		} else {
			return unauthorizedResponse();
		}
    }
	
	@RequestMapping(value = "rest/{token}/reports", method = RequestMethod.GET)
	public @ResponseBody Response<List<ReportModel>> reports(@PathVariable String token) {
		User user = authService.getUserByToken(token);
		if (user == null) {
			return unauthorizedResponse();
		}
		DTOMapper mapper = mappersRegistry.getMapper(ReportModel.class);
		return buildResponse(mapper.fillModels(reportService.getReportsByUsername(user.getUsername()), ReportModel.class));
	}
	
	@RequestMapping(value = "rest/{token}/report/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> report(@PathVariable String token, @PathVariable ObjectId id) {
		User user = authService.getUserByToken(token);
		if (user == null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "text/html");
			return new ResponseEntity<byte[]>("File not found".getBytes(), responseHeaders, HttpStatus.FORBIDDEN);
		}
		ReportExecution reportExecution = reportService.getReportExecution(id);
		if (reportExecution == null || !user.getUsername().equals(reportExecution.getUsername())) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "text/html");
			return new ResponseEntity<byte[]>("File not found".getBytes(), responseHeaders, HttpStatus.NOT_FOUND);
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Disposition", "attachment;filename="+reportExecution.getFileName().replaceAll("zip", "pdf"));
		responseHeaders.set("Content-Type", "application/pdf");
		return new ResponseEntity<byte[]>(reportExecution.getReport(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * Registra un dispositivo
	 * @param token token de autenticacion.
	 * @return
	 */
	@RequestMapping(value = "rest/{token}/register/{regid}", method = RequestMethod.GET)
    public @ResponseBody Response<String> register(@PathVariable String token, @PathVariable String regid) {
		User user = authService.getUserByToken(token);
		if (user == null) {
			return unauthorizedResponse();
		}
		gcmService.registerDevice(user.getUsername(), regid);
		return new Response<String>(Response.OK_RESPONSE);
	}
	
	@RequestMapping(value = "rest/{token}/send/{type}", method = RequestMethod.GET)
    public @ResponseBody Response<String> send(@PathVariable String token, @PathVariable String type) {
		User user = authService.getUserByToken(token);
		if (user == null) {
			return unauthorizedResponse();
		}
		GCMMessage msg = new GCMSimpleMessage(type, "hola mundo con GCM!!!");
		gcmService.sendMessage(msg, user);
		return new Response<String>(OK_RESPONSE, null, SUCCESSFUL);
	}
	
	private <T> Response<T> unauthorizedResponse() {
		Response<T> response = new Response<T>(UNAUTHRORIZED, null, INVALID_CREDENTIALS);
		return response;
	}
	
	private <T> Response<T> buildResponse(T data) {
		Response<T> response = new Response<T>(OK_RESPONSE, data, SUCCESSFUL);
		return response;
	}
}
