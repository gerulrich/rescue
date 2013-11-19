package net.cloudengine.service.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 
 * @author German Ulrich
 *
 */
public class CurrentUserArgumentResolver implements WebArgumentResolver {

	private SessionService service;
	
	@Autowired
    public CurrentUserArgumentResolver(SessionService service) {
		super();
		this.service = service;
	}

	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        CurrentUser currentUserAnnotation = methodParameter.getParameterAnnotation(CurrentUser.class);
        if(currentUserAnnotation != null) {
        	return service.getCurrentUser();
        }
        return UNRESOLVED;
    }
}