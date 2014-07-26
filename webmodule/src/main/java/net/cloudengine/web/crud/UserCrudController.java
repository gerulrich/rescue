package net.cloudengine.web.crud;

import javax.validation.Valid;

import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.User;
import net.cloudengine.web.crud.support.CrudAction;
import net.cloudengine.web.crud.support.CrudControllerDelegate;
import net.cloudengine.web.crud.support.CrudInterface;
import net.cloudengine.web.crud.support.EntityEditor;
import net.cloudengine.web.crud.support.ObjectIdParser;

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
@RequestMapping("/user")
public class UserCrudController implements CrudInterface<User, ObjectId> {

	private CrudControllerDelegate<User, ObjectId> delegate;
	private RepositoryLocator repositoryLocator;
	
	@Autowired
	public UserCrudController(RepositoryLocator repositoryLocator) {
		this.repositoryLocator = repositoryLocator;
		this.delegate = new CrudControllerDelegate<User, ObjectId>(User.class, "user", repositoryLocator);
		this.delegate.addCrudAction(CrudAction.VIEW, "/user/show/", "crud.view");
		this.delegate.addCrudAction(CrudAction.EDIT, "/user/edit/", "crud.edit");
		this.delegate.addCrudAction(CrudAction.SECUTIRY, "/user/password/", "crud.password");
		this.delegate.addCrudAction(CrudAction.DELETE, "/user/delete/", "crud.delete");
		this.delegate.addCrudAction(CrudAction.LIST, "/user/list", "crud.user.list");
		this.delegate.addCrudAction(CrudAction.ADD, "/user/new", "crud.user.new");
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "password");
		binder.registerCustomEditor(Group.class, new EntityEditor<Group,ObjectId>(repositoryLocator, new ObjectIdParser(), Group.class));
	}

	@Override
	@ModelAttribute("entity")
	public User getEntity(WebRequest request) {
		String id = delegate.getId(request, "show/*", "edit/*", "password/*");
		if (id !=null) {
			return delegate.getEntity(new ObjectId(id));
		}
		return new User();
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
	public ModelAndView show(@PathVariable("id") ObjectId pk) {
		return delegate.showEntity(pk);
	}

	@Override
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") ObjectId pk) {
		return delegate.deleteEntity(pk);
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView newSetupForm() {
		ModelAndView mav =  delegate.setupForm(null);
		delegate.addDependency(mav, Group.class, "groups");
		return mav;
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView newSubmitForm(@Valid @ModelAttribute("entity") User entity, BindingResult result) {
		return delegate.saveEntity(entity, result);
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView editSetupForm(@PathVariable("id") ObjectId pk) {
		ModelAndView mav =  delegate.setupForm(pk);
		delegate.addDependency(mav, Group.class, "groups");
		return mav;
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public ModelAndView editSubmitForm(@PathVariable("id") ObjectId pk, @Valid @ModelAttribute("entity") User entity, BindingResult result) {
		return delegate.updateEntity(entity, result);
	}
}
