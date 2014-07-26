package net.cloudengine.web.crud.support;

import java.io.Serializable;
import java.util.Map;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;

import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

public class CrudControllerDelegate<E, PK extends Serializable> {

	private Repository<E,PK> repository;
	private String entityName;
	private GridViewModel gridViewModel;
	private RepositoryLocator repositoryLocator;
	
	public CrudControllerDelegate(Class<E> clazz, String entityName, RepositoryLocator repositoryLocator) {
		super();
		this.repositoryLocator = repositoryLocator;
		this.repository = repositoryLocator.getRepository(clazz);
		this.entityName = entityName;
		try {
			this.gridViewModel = CrudUtil.getGridViewModel(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addCrudAction(int action, String url, String messageKey) {
		gridViewModel.addAction(new CrudAction(action, url, messageKey));
	}
	
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(String.format("redirect:/%s/list/1/10", entityName));
		return mav;
	}
	
	public ModelAndView list(int page, int size) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("entities", repository.list(page, size));
		mav.addObject("gridModel", gridViewModel);
		mav.setViewName("/crud/list");
		return mav;
	}
	
	public ModelAndView showEntity(PK id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("gridModel", gridViewModel);
		mav.setViewName(String.format("/crud/%s/show", entityName));
		return mav;
	}
	
	public ModelAndView setupForm(PK id) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(String.format("/crud/%s/form", entityName));
		return mav;
	}	
	
	public ModelAndView updateEntity(E entity, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.setViewName(String.format("/crud/%s/form", entityName));
		} else {
			mav.setViewName(String.format("redirect:/%s/list", entityName));
			repository.update(entity);
		}
		return mav;
	}
	
	public ModelAndView saveEntity(E entity, BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.setViewName(String.format("/crud/%s/form", entityName));
		} else {
			mav.setViewName(String.format("redirect:/%s/list", entityName));
			repository.save(entity);
		}
		return mav;
	}
	
	public ModelAndView deleteEntity(PK id) {
		ModelAndView mav = new ModelAndView();
		repository.delete(id);
		mav.setViewName(String.format("redirect:/%s/list", entityName));		
		return mav;
	}
	
	public E getEntity(PK id) {
		return repository.get(id);
	}
	
	public String getId(WebRequest request, String... patterns) {
		String uri =  request.getDescription(false).replaceFirst("uri=", "");
		String id = null;
		for(int i = 0; i < patterns.length && id==null; i++) {
			String names[] = {"id"};
			Map<String, String> pathMap = PathUtil.getPathVariables(patterns[i], names, uri);
			id = pathMap.get("id");
		}
		return id;
	}
	
	public void addDependency(ModelAndView mav, Class<?> clazz, String name) {
		mav.addObject(name, repositoryLocator.getRepository(clazz).getAll());
	}
}
