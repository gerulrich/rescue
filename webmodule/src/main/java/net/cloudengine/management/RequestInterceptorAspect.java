package net.cloudengine.management;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.statistics.RequestLog;
import net.cloudengine.service.web.SessionService;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
public class RequestInterceptorAspect {

	private Datastore<RequestLog, ObjectId> ds;
	private ExecutorService executorService;
	private SessionService sessionService;
	private ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

	@Pointcut("execution(public * *(..))")
	private void anyPublicOperation() { }

	@Before("anyPublicOperation() && @annotation(requestMapping)")
	public void interceptRequestBefore(RequestMapping requestMapping) {
		threadLocal.set(System.currentTimeMillis());
	}

	@AfterReturning("anyPublicOperation() && @annotation(requestMapping)")
	public void interceptRequestAfter(JoinPoint joinPoint, RequestMapping requestMapping) {
		long start = threadLocal.get();
		threadLocal.remove();
		
		if (joinPoint.getSignature() instanceof MethodSignature) {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			if (signature.getMethod().getAnnotation(IgnoreTrace.class) != null) {
				return;
			}
		}		
		
		saveLog(start, joinPoint.getTarget(), joinPoint.getSignature().getName(), null);
	}

	@AfterThrowing(pointcut = "anyPublicOperation() && @annotation(requestMapping)", throwing = "t")
	public void interceptRequestError(JoinPoint joinPoint, Throwable t, RequestMapping requestMapping) {
		long start = threadLocal.get();
		threadLocal.remove();
		saveLog(start, joinPoint.getTarget(), joinPoint.getSignature().getName(), t);
	}
	
	private void saveLog(long startTime, Object controller, String urlPattern, Throwable t) {
		String method = urlPattern;
		String controllerName = controller.getClass().getSimpleName();
		RequestLog log = new RequestLog();
		log.setController(controllerName);
		log.setMethod(method);
		// log.setUrl(getFullURL(request));
		log.setTime(new Date(startTime));
		log.setExecutionTime(System.currentTimeMillis() - startTime);
		log.setUser(getUsername());
		if (t != null) {
			log.setStatus("ERROR");
			String errors[] = ExceptionUtils.getStackTrace(t).split("\n"); 
			log.setErrors(errors);
		} else {
			log.setStatus("OK");
		}
		saveLog(log);
	}
	
	private String getUsername() {
		return sessionService.getCurrentUser() != null ?
			sessionService.getCurrentUser().getUsername() : "anonimous";
	}
	
	private void saveLog(final RequestLog log) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				ds.save(log);
			}
		});		
	}
	
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void setDs(Datastore<RequestLog, ObjectId> ds) {
		this.ds = ds;
	}
}
