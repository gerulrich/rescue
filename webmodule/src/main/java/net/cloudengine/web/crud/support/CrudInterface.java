package net.cloudengine.web.crud.support;

import java.io.Serializable;

import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

public interface CrudInterface<E, PK extends Serializable> {
	
	E getEntity(WebRequest request);
	ModelAndView list();
	ModelAndView list(int page, int size);
	ModelAndView show(PK pk);
	ModelAndView delete(PK pk);
	ModelAndView newSetupForm();
	ModelAndView newSubmitForm(E entity, BindingResult result);
	ModelAndView editSetupForm(PK pk);
	ModelAndView editSubmitForm(PK pk, E entity, BindingResult result);

}
