package net.cloudengine.web.crud;

import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.domain.workflow.PersistentWorkflow;
import net.cloudengine.service.WorkflowService;
import net.cloudengine.web.crud.support.CrudAction;
import net.cloudengine.web.crud.support.CrudControllerDelegate;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("workflow")
public class WorkflowCrudController /*implements CrudInterface<PersistentWorkflow, ObjectId>*/ {

	private CrudControllerDelegate<PersistentWorkflow, ObjectId> delegate;
	private WorkflowService workflowService;	
	
	@Autowired
	public WorkflowCrudController(WorkflowService workflowService, RepositoryLocator repositoryLocator) {
		this.workflowService = workflowService;
		this.delegate = new CrudControllerDelegate<PersistentWorkflow, ObjectId>(PersistentWorkflow.class, "workflow", repositoryLocator);
		this.delegate.addCrudAction(CrudAction.LIST, "/workflow/list", "crud.workflow.list");
		this.delegate.addCrudAction(CrudAction.CUSTOM, "/workflow/activate/", "crud.workflow.activate");
		this.delegate.addCrudAction(CrudAction.VIEW, "/workflow/show/", "crud.workflow.activate");
	}

//	@Override
	@ModelAttribute("entity")
	public PersistentWorkflow getEntity(WebRequest request) {
		String id = delegate.getId(request, "show/*");
		if (id !=null) {
			return delegate.getEntity(new ObjectId(id));
		}
		return null;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		return delegate.list();
	}

	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		return delegate.list(page, size);
	}

	@RequestMapping(value = "activate/{id}", method = RequestMethod.GET)
	public ModelAndView activate(@PathVariable ObjectId id) {
		workflowService.activateWorkflow(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/workflow/list");
		return mav;
	}

	@RequestMapping(value = "show/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable ObjectId id) {
		return delegate.showEntity(id);
	}

}
