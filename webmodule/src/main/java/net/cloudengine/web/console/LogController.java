package net.cloudengine.web.console;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import net.cloudengine.api.Query;
import net.cloudengine.api.mongo.dao.RequestLogDao;
import net.cloudengine.management.IgnoreTrace;
import net.cloudengine.model.statistics.RequestLog;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogController {

	private static final String ERROR_STATUS = "ERROR";
	private static final String OK_STATUS = "OK";
	private RequestLogDao dao;

	@Autowired
	public LogController(RequestLogDao dao) {
		super();
		this.dao = dao;
	}
	
	/**
	 * 
	 */
	@RequestMapping(value="/log/save/{status}", method = RequestMethod.GET)
	@IgnoreTrace
	public @ResponseBody String saveLog(@PathVariable("status") String status) {
		RequestLog log = new RequestLog();
		log.setStatus(status);
		log.setExecutionTime(100L);
		log.setTime(new Date());
		log.setController("LogController");
		log.setMethod("saveLog");
		log.setUser("anonimous");
		if (ERROR_STATUS.equals(status)) {
			log.setStatus(ERROR_STATUS);
			log.setErrors(ExceptionUtils.getStackFrames(new RuntimeException("Dummy Exception")));
		} else {
			log.setStatus(OK_STATUS);
		}
		dao.save(log);
		return "OK";
	}
	
	@RequestMapping(value="/log/drop/", method = RequestMethod.GET)
	@IgnoreTrace
	public @ResponseBody String dropLog() {
		Query<RequestLog> query = dao.createQuery().field("controller").eq("LogController");
		dao.delete(query);
		return "OK";
	}
	
	@RequestMapping(value="/log/get/{controller}/{method}", method = RequestMethod.GET)
	@IgnoreTrace
	public ResponseEntity<byte[]> getLog(@PathVariable("controller") String controller, @PathVariable("method") String method) {
		Collection<RequestLog> logs = dao.getRequestLog(controller, method);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		try {
			for(RequestLog log : logs) {
				String data = String.format("FECHA: %s - USUARIO: %s\n", format.format(log.getTime()), log.getUser());
				os.write(data.getBytes());
				if (log.getErrors() != null) {
					for(String error : log.getErrors()) {
						os.write((error+"\n").getBytes());
					}
				}
				os.write("------------------------------\n".getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Disposition", "attachment;filename="+controller+"_"+method+".log");
		responseHeaders.set("Content-Type", "text/plain");
		return new ResponseEntity<byte[]>(os.toByteArray(), responseHeaders, HttpStatus.OK);
		
	}

}
