package net.cloudengine.web.crud.workflow;

import net.cloudengine.api.Datastore;
import net.cloudengine.domain.dsl.workflow.PersistentWorkflow;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ListWorkflowController {
	
	private Datastore<PersistentWorkflow, ObjectId> workflowDao;
	
	@Autowired
	public ListWorkflowController(@Qualifier("wfStore") Datastore<PersistentWorkflow, ObjectId> workflowDao) {
		super();
		this.workflowDao = workflowDao;
	}

	@RequestMapping(value = "/workflow/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/workflow/list/1/10");
		return mav;
	}
	
	@RequestMapping(value = "/workflow/list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("workflows", workflowDao.list(page, size));
		mav.setViewName("/crud/workflow/list");
		return mav;
	}
}