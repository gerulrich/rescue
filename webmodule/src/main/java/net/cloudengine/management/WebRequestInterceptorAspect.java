package net.cloudengine.management;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.RequestMapping;

import com.newrelic.api.agent.NewRelic;

@Aspect
public class WebRequestInterceptorAspect {

	@Pointcut("within(@net.cloudengine.management.ExternalService *)")
	public void beanAnnotatedWithExternalService() {}
	
	@Pointcut("execution(public * *(..))")
	private void anyPublicOperation() { }

	@Before("anyPublicOperation() && @annotation(requestMapping)")
	public void interceptWebMethod(JoinPoint joinPoint, RequestMapping requestMapping) {
		if (joinPoint == null || requestMapping == null) {
			return;
		}
		
		String controllerName = joinPoint.getTarget().getClass().getSimpleName();
		String method = joinPoint.getSignature().getName();
		NewRelic.setTransactionName(null, "/"+controllerName+"/"+method);
		NewRelic.incrementCounter("Custom/request/web");
		
	}
	
	@Before("anyPublicOperation() && beanAnnotatedWithExternalService()")
	public void interceptRPCMethod(JoinPoint joinPoint) {
		if (joinPoint == null) {
			return;
		}
		
		String controllerName = joinPoint.getTarget().getClass().getSimpleName();
		String method = joinPoint.getSignature().getName();
		NewRelic.setTransactionName(null, "/"+controllerName+"/"+method);
		NewRelic.incrementCounter("Custom/request/rpc");
		
	}	

}
