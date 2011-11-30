package net.cloudengine.web.servlet;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 
 * @author German Ulrich
 *
 */
public class CurrentUserArgumentResolver implements WebArgumentResolver {

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        CurrentUser currentUserAnnotation = methodParameter.getParameterAnnotation(CurrentUser.class);
        if(currentUserAnnotation != null) {
        	return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return UNRESOLVED;
    }
}