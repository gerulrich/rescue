package net.cloudengine.web.crud.group;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.auth.Group;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/group")
public class ListGroupController {
	
	private Repository<Group,ObjectId> groupRepository;

	@Autowired
	public ListGroupController(RepositoryLocator repositoryLocator) {
		super();
		this.groupRepository = repositoryLocator.getRepository(Group.class);
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/group/list/1/10");
		return mav;
	}
	
	@RequestMapping(value = "list/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("page") int page, @PathVariable("size") int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("groups", groupRepository.list(page, size));
		mav.setViewName("/crud/group/list");
		return mav;
	}
	
	@RequestMapping(value = "show/{id}", method = RequestMethod.GET)
	public ModelAndView showGroup(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("group", groupRepository.get(id));
		mav.setViewName("/crud/group/show");
		return mav;
	}

}
