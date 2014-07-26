package net.cloudengine.web.crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.Role;
import net.cloudengine.model.auth.SelectablePermission;
import net.cloudengine.web.crud.support.CrudAction;
import net.cloudengine.web.crud.support.CrudControllerDelegate;
import net.cloudengine.web.crud.support.CrudInterface;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/role")
public class RoleCrudController implements CrudInterface<Role, ObjectId> {
	
	private CrudControllerDelegate<Role, ObjectId> delegate;
	private Repository<Permission,ObjectId> permissionRepository;
	
	@Autowired
	public RoleCrudController(RepositoryLocator repositoryLocator) {
		this.permissionRepository = repositoryLocator.getRepository(Permission.class);
		delegate = new CrudControllerDelegate<Role, ObjectId>(Role.class, "role", repositoryLocator);
		delegate.addCrudAction(CrudAction.VIEW, "/role/show/", "crud.view");
		delegate.addCrudAction(CrudAction.EDIT, "/role/edit/", "crud.edit");
		delegate.addCrudAction(CrudAction.SECUTIRY, "/role/permissions/edit/", "crud.permissions");
		delegate.addCrudAction(CrudAction.DELETE, "/role/delete/", "crud.delete");
		delegate.addCrudAction(CrudAction.ADD, "/role/new", "crud.role.new");
		delegate.addCrudAction(CrudAction.LIST, "/role/list", "crud.role.list");
	}
	
	@Override
	@ModelAttribute("entity")
	public Role getEntity(WebRequest request) {
		String id = delegate.getId(request, "show/*", "edit/*");
		if (id !=null) {
			return delegate.getEntity(new ObjectId(id));
		}
		return new Role();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "permissions");
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
	public ModelAndView newSubmitForm(@Valid @ModelAttribute("entity") Role role, BindingResult result) {
		return delegate.saveEntity(role, result);
	}
	
	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView editSetupForm(@PathVariable ObjectId id) {
		return delegate.setupForm(id);
	}
	
	@Override
	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public ModelAndView editSubmitForm(@PathVariable ObjectId id, @Valid @ModelAttribute("entity") Role role, BindingResult result) {
		return delegate.saveEntity(role, result);
	}
	
	@RequestMapping(value = "permissions/edit/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView("/crud/role/permissions");
		Role role = delegate.getEntity(id);
		List<Permission> permissions = permissionRepository.getAll();
		List<SelectablePermission> allPermissions = new ArrayList<SelectablePermission>();
		for(Permission permission : permissions) {
			SelectablePermission p = new SelectablePermission();
			p.setId(permission.getId());
			p.setName(permission.getName());
			p.setDescription(permission.getDescription());
			p.setSelected(role.getPermissions().contains(permission));
			allPermissions.add(p);
		}		
		mav.addObject("permissions", allPermissions);
		mav.addObject("role", role);
		return mav;
	}
	
	@RequestMapping(value = "permissions/edit/{roleId}", method = RequestMethod.POST)
	public ModelAndView submitForm(@PathVariable("roleId") ObjectId roleId, @RequestParam(value="permissionId", required=false) ObjectId[] ids) {
		
		List<ObjectId> idList = new ArrayList<ObjectId>();
		if (ids != null && ids.length > 0) {
			idList.addAll(Arrays.asList(ids));
		}
		
		Role role = delegate.getEntity(roleId);
		List<Permission> newPermissionList = new ArrayList<Permission>(role.getPermissions()); 
		
		Iterator<Permission> iterator = newPermissionList.iterator();
		for(; iterator.hasNext();) {
			Permission current = iterator.next();
			if (idList.contains(current.getId())) {
				// El permiso esta asignado.
				idList.remove(current.getId());
			} else {
				// El permiso ya no está más asignado.
				iterator.remove();
			}
		}
		
		// Permisos nuevos a asignar.
		for(ObjectId id : idList) {
			Permission newPermission = permissionRepository.get(id);
			newPermissionList.add(newPermission);
		}
		role.setPermissions(newPermissionList);
		permissionRepository.update(role);		
		
		ModelAndView mav = new ModelAndView("redirect:/role/show/"+roleId);
		return mav;
	}
}
