package net.cloudengine.web.crud.workflow;

import net.cloudengine.service.workflow.WorkflowService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewWorkflowController {
	
	private WorkflowService workflowService;
	
	@Autowired
	public NewWorkflowController(WorkflowService workflowService) {
		super();
		this.workflowService = workflowService;
	}

	@RequestMapping(value = "/workflow/new", method = RequestMethod.GET)
	public ModelAndView setupForm() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("files", workflowService.getWorkflowFiles());
		mav.setViewName("/crud/workflow/form");
		return mav;
	}
	
	@RequestMapping(value = "/workflow/new", method = RequestMethod.POST)
	public ModelAndView submit(@RequestParam("fileId") ObjectId fileId) {
		ModelAndView mav = new ModelAndView();
		try {
			workflowService.createFromFile(fileId);
			mav.setViewName("redirect:/workflow/list");
		} catch (Exception e) {
			mav.addObject("error", e.getMessage());
			mav.setViewName("/crud/workflow/error");
		}
		return mav;
	}
	
	@RequestMapping(value = "/workflow/activate/{id}", method = RequestMethod.GET)
	public ModelAndView activate(@PathVariable ObjectId id) {
		workflowService.activateWorkflow(id);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/workflow/list");
		return mav;
	}
}