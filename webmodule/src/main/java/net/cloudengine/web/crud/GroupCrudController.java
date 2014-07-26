package net.cloudengine.web.crud;

import javax.validation.Valid;

import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.auth.Group;
import net.cloudengine.web.crud.support.CrudAction;
import net.cloudengine.web.crud.support.CrudControllerDelegate;
import net.cloudengine.web.crud.support.CrudInterface;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/group")
public class GroupCrudController implements CrudInterface<Group, ObjectId> {

	private CrudControllerDelegate<Group, ObjectId> delegate;

	@Autowired
	public GroupCrudController(RepositoryLocator repositoryLocator) {
		delegate = new CrudControllerDelegate<Group, ObjectId>(Group.class, "group", repositoryLocator);
		delegate.addCrudAction(CrudAction.VIEW, "/group/show/", "crud.view");
		delegate.addCrudAction(CrudAction.EDIT, "/group/edit/", "crud.edit");
		delegate.addCrudAction(CrudAction.DELETE, "/group/delete/", "crud.delete");
		delegate.addCrudAction(CrudAction.ADD, "/group/new", "crud.group.new");
		delegate.addCrudAction(CrudAction.LIST, "/group/list", "crud.group.list");
	}
	
	@Override
	@ModelAttribute("entity")
	public Group getEntity(WebRequest request) {
		String id = delegate.getId(request, "show/*", "edit/*");
		if (id !=null) {
			return delegate.getEntity(new ObjectId(id));
		}
		return new Group();
	}

	@Override
	@RequestMapping(value = "list", method = RequestMethod.GET)
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
	public ModelAndView show(@PathVariable("id") ObjectId id) {
		return delegate.showEntity(id);
	}

	@Override
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") ObjectId id) {
		return delegate.deleteEntity(id);
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView newSetupForm() {
		return delegate.setupForm(null);
	}
	
	@Override
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView newSubmitForm(@Valid @ModelAttribute("group") Group group, BindingResult result) {
		return delegate.saveEntity(group, result);
	}
	
	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView editSetupForm(@PathVariable ObjectId id) {
		return delegate.setupForm(id);
	}
	
	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public ModelAndView editSubmitForm(@PathVariable ObjectId id, @Valid @ModelAttribute("group") Group group, BindingResult result) {
		return delegate.saveEntity(group, result);
	}
}
