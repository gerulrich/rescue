package net.cloudengine.web.crud;

import javax.validation.Valid;

import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.resource.ResourceType;
import net.cloudengine.service.ResourceService;
import net.cloudengine.web.crud.support.CrudAction;
import net.cloudengine.web.crud.support.CrudControllerDelegate;
import net.cloudengine.web.crud.support.CrudInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/resource/type")
public class ResourceTypeCrudController implements CrudInterface<ResourceType, Long> {

	private CrudControllerDelegate<ResourceType, Long> delegate;
	private ResourceService resourceService;
	private RepositoryLocator repositoryLocator;
	
	@Autowired
	public ResourceTypeCrudController(ResourceService resourceService, RepositoryLocator repositoryLocator) {
		this.resourceService = resourceService;
		this.repositoryLocator = repositoryLocator;
		this.delegate = new CrudControllerDelegate<ResourceType, Long>(ResourceType.class, "resource/type", repositoryLocator);
		delegate.addCrudAction(CrudAction.VIEW, "/resource/type/show/", "crud.view");
		delegate.addCrudAction(CrudAction.EDIT, "/resource/type/edit/", "crud.edit");
		delegate.addCrudAction(CrudAction.DELETE, "/resource/type/delete/", "crud.delete");
		delegate.addCrudAction(CrudAction.ADD, "/resource/type/new", "crud.resourceType.new");
		delegate.addCrudAction(CrudAction.LIST, "/resource/type/list", "crud.resourceType.list");		
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
		binder.registerCustomEditor(byte[].class, new ImageEditor(repositoryLocator));
	}
	
	@Override
	@ModelAttribute("entity")
	public ResourceType getEntity(WebRequest request) {
		String id = delegate.getId(request, "show/*", "edit/*");
		if (id !=null) {
			return delegate.getEntity(Long.parseLong(id));
		}
		return new ResourceType();
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
		mav.addObject("files", resourceService.getAllImages());
		return mav;
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView newSubmitForm(@Valid @ModelAttribute("entity") ResourceType entity, BindingResult result) {
		return delegate.saveEntity(entity, result);
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView editSetupForm(@PathVariable("id") Long pk) {
		ModelAndView mav = delegate.setupForm(pk);
		mav.addObject("files", resourceService.getAllImages());
		return mav;
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)	
	public ModelAndView editSubmitForm(@PathVariable("id") Long pk, @Valid @ModelAttribute("entity") ResourceType entity, BindingResult result) {
		return delegate.updateEntity(entity, result);
	}
	
	@RequestMapping(value = "image/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> showResourceTypeImage(@PathVariable("id") Long id) {
		ResourceType type = delegate.getEntity(id);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "image/png");
		return new ResponseEntity<byte[]>(type.getImage(), responseHeaders, HttpStatus.OK);
	}

}
