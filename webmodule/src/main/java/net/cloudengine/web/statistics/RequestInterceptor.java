package net.cloudengine.web.statistics;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.statistics.RequestLog;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestInterceptor extends HandlerInterceptorAdapter {

	private Datastore<RequestLog, ObjectId> ds;
	
	@Autowired
	public RequestInterceptor(@Qualifier("requestLogStore") Datastore<RequestLog, ObjectId> ds) {
		super();
		this.ds = ds;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		
		if (!request.getRequestURI().contains("/stats")) {
			RequestLog log = new RequestLog();
			log.setTime(new Date());
			ds.save(log);
		}
	}
	
	

}
