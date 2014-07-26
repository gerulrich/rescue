package net.cloudengine.web.crud;

import javax.validation.Valid;

import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.config.AppProperty;
import net.cloudengine.web.crud.support.CrudAction;
import net.cloudengine.web.crud.support.CrudControllerDelegate;
import net.cloudengine.web.crud.support.CrudInterface;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/config")
public class PropertyCrudController implements CrudInterface<AppProperty, ObjectId> {

	private CrudControllerDelegate<AppProperty, ObjectId> delegate;
	
	@Autowired
	public PropertyCrudController(RepositoryLocator repositoryLocator) {
		delegate = new CrudControllerDelegate<AppProperty, ObjectId>(AppProperty.class, "config", repositoryLocator);
		delegate.addCrudAction(CrudAction.EDIT, "/config/edit/", "crud.edit");
		delegate.addCrudAction(CrudAction.LIST, "/config/list", "crud.config.list");
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "key");
	}
	
	@Override
	@ModelAttribute("entity")
	public AppProperty getEntity(WebRequest request) {
		String id = delegate.getId(request, "edit/*");
		if (id !=null) {
			return delegate.getEntity(new ObjectId(id));
		}
		return new AppProperty();
	}

	@Override
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		return delegate.list();
	}

	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		return delegate.list(page, size);
	}

	@Override
	public ModelAndView show(ObjectId pk) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ModelAndView delete(ObjectId pk) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ModelAndView newSetupForm() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ModelAndView newSubmitForm(AppProperty entity, BindingResult result) {
		throw new UnsupportedOperationException();
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView editSetupForm(ObjectId pk) {
		return delegate.setupForm(pk);
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public ModelAndView editSubmitForm(ObjectId pk, @Valid @ModelAttribute("entity") AppProperty entity, BindingResult result) {
		return delegate.updateEntity(entity, result);
	}

}
