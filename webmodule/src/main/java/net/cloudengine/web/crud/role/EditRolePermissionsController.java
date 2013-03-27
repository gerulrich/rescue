package net.cloudengine.web.crud.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.cloudengine.api.Datastore;
import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.Role;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller que permite editar los permisos de los roles.
 * @author German
 *
 */
@Controller
@RequestMapping("/role/permissions")
public class EditRolePermissionsController {

	private Datastore<Role,ObjectId> datastore;
	private Datastore<Permission,ObjectId> permissionStore;

	@Autowired
	public EditRolePermissionsController(
			@Qualifier("roleStore") Datastore<Role, ObjectId> datastore,
			@Qualifier("permissionStore") Datastore<Permission,ObjectId> permissionStore) {
		super();
		this.datastore = datastore;
		this.permissionStore = permissionStore;
	}
	
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView setupForm(@PathVariable("id") ObjectId id) {
		ModelAndView mav = new ModelAndView("/crud/role/permissions");
		Role role = datastore.get(id);
		List<Permission> permissions = permissionStore.getAll();
		
		// permisos que tiene el usuario
		List<ExtendedPermission> allpermissions = new ArrayList<ExtendedPermission>();
		for (Permission selper : new ArrayList<Permission>(role.getPermissions())) {
			ExtendedPermission p = new ExtendedPermission();
			p.setId(selper.getId());
			p.setName(selper.getName());
			p.setDescription(selper.getDescription());
			p.setSelected(true);
			allpermissions.add(p);
		}
		
		// permisos que no tiene el usuarios
		permissions.removeAll(role.getPermissions());
		for (Permission noselper : permissions) {
			ExtendedPermission p = new ExtendedPermission();
			p.setId(noselper.getId());
			p.setName(noselper.getName());
			p.setDescription(noselper.getDescription());
			p.setSelected(false);
			allpermissions.add(p);
		}
		
		mav.addObject("permissions", allpermissions);
		mav.addObject("role", role);
		return mav;
	}
	
	@RequestMapping(value = "edit/{roleId}", method = RequestMethod.POST)
	public ModelAndView submitForm(@PathVariable("roleId") ObjectId roleId, @RequestParam(value="permissionId", required=false) ObjectId[] ids) {
		
		List<ObjectId> idList = new ArrayList<ObjectId>();
		if (ids != null && ids.length > 0) {
			idList.addAll(Arrays.asList(ids));
		}
		Role role = datastore.get(roleId);
		List<Permission> actualPermissions = new ArrayList<Permission>(role.getPermissions()); 
		
		Iterator<Permission> iterator = actualPermissions.iterator();
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
			Permission newPermission = permissionStore.get(id);
			actualPermissions.add(newPermission);
		}
		role.setPermissions(actualPermissions);
		datastore.update(role);
		
		
		ModelAndView mav = new ModelAndView("redirect:/role/show/"+roleId);
		return mav;
	}
	
}
