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
public class DeleteGroupController {
	
	private Repository<Group,ObjectId> groupRepository;

	@Autowired
	public DeleteGroupController(RepositoryLocator repositoryLocator) {
		super();
		this.groupRepository = repositoryLocator.getRepository(Group.class);
	}
	
	@RequestMapping(value = "/group/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteUser(@PathVariable("id") ObjectId id) {
		groupRepository.delete(id);
		return new ModelAndView("redirect:/group/list");
	}

}
