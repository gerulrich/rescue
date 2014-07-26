package net.cloudengine.web.crud;

import javax.validation.Valid;

import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.resource.Resource;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.web.crud.support.CrudAction;
import net.cloudengine.web.crud.support.CrudControllerDelegate;
import net.cloudengine.web.crud.support.CrudInterface;
import net.cloudengine.web.crud.support.EntityEditor;
import net.cloudengine.web.crud.support.LongIdParser;

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
@RequestMapping("/resource")
public class ResourceCrudController implements CrudInterface<Resource,Long> {

	private CrudControllerDelegate<Resource, Long> delegate;
	private RepositoryLocator repositoryLocator;
	
	@Autowired
	public ResourceCrudController(RepositoryLocator repositoryLocator) {
		this.repositoryLocator = repositoryLocator;
		this.delegate = new CrudControllerDelegate<Resource, Long>(Resource.class, "resource", repositoryLocator);
		delegate.addCrudAction(CrudAction.VIEW, "/resource/show/", "crud.view");
		delegate.addCrudAction(CrudAction.EDIT, "/resource/edit/", "crud.edit");
		delegate.addCrudAction(CrudAction.DELETE, "/resource/delete/", "crud.delete");
		delegate.addCrudAction(CrudAction.ADD, "/resource/new", "crud.resource.new");
		delegate.addCrudAction(CrudAction.LIST, "/resource/list", "crud.resource.list");
	}
	
	@InitBinder
	public void initBinder(WebDataBinder b) {
	    b.registerCustomEditor(ResourceType.class, new EntityEditor<ResourceType,Long>(repositoryLocator, new LongIdParser(), ResourceType.class));
	}	

	@Override
	@ModelAttribute("entity")
	public Resource getEntity(WebRequest request) {
		String id = delegate.getId(request, "show/*", "edit/*");
		if (id !=null) {
			return delegate.getEntity(Long.parseLong(id));
		}
		return new Resource();
	}

	@Override
	@RequestMapping(value="list", method = RequestMethod.GET)
	public ModelAndView list() {
		return delegate.list();
	}

	@Override
	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		return delegate.list(page, size);
	}
	
	@Override
	@RequestMapping(value = "show/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") Long pk) {
		return delegate.showEntity(pk);
	}

	@Override
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long pk) {
		return delegate.deleteEntity(pk);
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.GET)	
	public ModelAndView newSetupForm() {
		ModelAndView mav = delegate.setupForm(null);
		delegate.addDependency(mav, ResourceType.class, "types");
		return mav;
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.POST)	
	public ModelAndView newSubmitForm(@Valid @ModelAttribute("entity") Resource entity, BindingResult result) {
		return delegate.saveEntity(entity, result);
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView editSetupForm(@PathVariable("id") Long pk) {
		ModelAndView mav = delegate.setupForm(pk);
		delegate.addDependency(mav, ResourceType.class, "types");
		return mav;
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public ModelAndView editSubmitForm(@PathVariable("id") Long pk, @Valid @ModelAttribute("entity") Resource entity, BindingResult result) {
		return delegate.updateEntity(entity, result);
	}

}
