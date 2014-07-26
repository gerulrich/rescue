package net.cloudengine.web.crud;

import javax.validation.Valid;

import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.chart.ChartInfo;
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
@RequestMapping("/chart")
public class ChartInfoCrudController implements CrudInterface<ChartInfo, ObjectId> {

	private CrudControllerDelegate<ChartInfo, ObjectId> delegate;
	
	@Autowired
	public ChartInfoCrudController(RepositoryLocator repositoryLocator) {
		this.delegate = new CrudControllerDelegate<ChartInfo, ObjectId>(ChartInfo.class, "chart", repositoryLocator);
		delegate.addCrudAction(CrudAction.EDIT, "/chart/edit/", "crud.edit");
		delegate.addCrudAction(CrudAction.DELETE, "/chart/delete/", "crud.delete");
		delegate.addCrudAction(CrudAction.ADD, "/chart/new", "crud.chart.new");
		delegate.addCrudAction(CrudAction.LIST, "/chart/list", "crud.chart.list");		
	}
	
	@Override
	@ModelAttribute("entity")
	public ChartInfo getEntity(WebRequest request) {
		String id = delegate.getId(request, "show/*", "edit/*");
		if (id !=null) {
			return delegate.getEntity(new ObjectId(id));
		}
		return new ChartInfo();
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
	public ModelAndView show(ObjectId pk) {
		throw new UnsupportedOperationException("Operation not supported");
	}

	@Override
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") ObjectId pk) {
		return delegate.deleteEntity(pk);
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView newSetupForm() {
		return delegate.setupForm(null);
	}

	@Override
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView newSubmitForm(@Valid @ModelAttribute("entity") ChartInfo entity, BindingResult result) {
		return delegate.saveEntity(entity, result);
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView editSetupForm(ObjectId pk) {
		return delegate.setupForm(pk);
	}

	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public ModelAndView editSubmitForm(ObjectId pk, ChartInfo entity, BindingResult result) {
		return delegate.updateEntity(entity, result);
	}

}
