package net.cloudengine.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cloudengine.model.auth.Group;
import net.cloudengine.model.auth.Permission;
import net.cloudengine.model.auth.User;
import net.cloudengine.service.UserService;
import net.cloudengine.util.Cipher;

import org.bson.types.ObjectId;
import org.mockito.Mockito;

public class TestUtil {
	
	static final Map<String, List<Permission>> permissions = new HashMap<String, List<Permission>>();
	static final List<User> users = new ArrayList<User>();
	
	public static final ObjectId GROUP_OBJECT_ID = new ObjectId();
	
	static {
		
		// inicializo los datos.
		String[][] usersData = {
				{"user1@test.com", "pass1"},
				{"user2@test.com", "pass2"},
				{"user3@test.com", "pass3"},
		};
		
		for(int i = 0; i < usersData.length; i++) {
			User user = new User();
			user.setId(new ObjectId());
			user.setUsername(usersData[i][0]);
			user.setPassword(new Cipher().encrypt(usersData[i][1]));
			users.add(user);
		}
		
		Object userPermissions[][] = {
			{"user1@test.com", new String[] {"DESKTOP", "ADMIN"}},
			{"user2@test.com", new String[] {"ADMIN"}},
			{"user2@test.com", new String[] {"ADMIN"}}
		};
		
		for (int i = 0; i < userPermissions.length; i++) {
			List<Permission> permissionList = new ArrayList<Permission>();
			String username = userPermissions[i][0].toString();
			String permissionsArray[] = (String[]) userPermissions[i][1];
			for(String permissionString : permissionsArray) {
				Permission permission = new Permission();
				permission.setName(permissionString);
				permission.setDescription(permissionString);
				permission.setId(new ObjectId());
				permissionList.add(permission);
			}
			permissions.put(username, permissionList);
		}
	}
	
	public static List<User> getUserList() {
		return users;
	}
	
	public static UserService getUserServiceMock() {
		UserService userService = Mockito.mock(UserService.class);
		
		for(User user : TestUtil.getUserList()) {
			Mockito
				.when(userService.getByUsername(Mockito.eq(user.getUsername())))
				.thenReturn(user);
			
//			Mockito
//				.when(userService.getPermissionForUser(Mockito.eq(user)))
//				.thenReturn(permissions.get(user.getUsername()));
			
		}
		
		return userService;
	}
	
//	/**
//	 * Mockea un {@link Datastore} para realizar la siguiente consulta:
//	 * Datastore<E,K> datastore = ...
//	 * datastore.createQuery().field("field").eq(value).get();
//	 * @param object
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public static <E,K extends Serializable> Datastore<E, K> mockDatastore(E object) {
//
//		Query<E> query = Mockito.mock(Query.class);
//		Field<E> field = Mockito.mock(Field.class);
//
//		Mockito.when(query.field(Mockito.anyString())).thenReturn(field);
//		Mockito.when(field.eq(Mockito.any())).thenReturn(query);
//		Mockito.when(field.le(Mockito.any())).thenReturn(query);
//		Mockito.when(field.lt(Mockito.any())).thenReturn(query);
//		Mockito.when(field.ge(Mockito.any())).thenReturn(query);
//		Mockito.when(field.gt(Mockito.any())).thenReturn(query);
//		Mockito.when(query.get()).thenReturn(object);
//
//		Datastore<E, K> datastore = Mockito.mock(Datastore.class);
//		Mockito
//			.when(datastore.createQuery())
//			.thenReturn(query);
//		return datastore;
//	}
	
//	@SuppressWarnings("unchecked")
//	public static <E,K extends Serializable> Datastore<E, K> mockDatastore(List<E> objects) {
//
//		Query<E> query = Mockito.mock(Query.class);
//		Field<E> field = Mockito.mock(Field.class);
//
//		Mockito.when(query.field(Mockito.anyString())).thenReturn(field);
//		Mockito.when(field.eq(Mockito.any())).thenReturn(query);
//		Mockito.when(field.le(Mockito.any())).thenReturn(query);
//		Mockito.when(field.lt(Mockito.any())).thenReturn(query);
//		Mockito.when(field.ge(Mockito.any())).thenReturn(query);
//		Mockito.when(field.gt(Mockito.any())).thenReturn(query);
//		
//		Mockito.when(query.list()).thenReturn(objects);
//
//		Datastore<E, K> datastore = Mockito.mock(Datastore.class);
//		Mockito
//			.when(datastore.createQuery())
//			.thenReturn(query);
//		return datastore;
//	}
	
	public static User createInstanceUser(String username) {
		Group group = new Group();
		group.setId(GROUP_OBJECT_ID);
		User user = new User();
		user.setUsername(username);
		user.setGroup(group);		
		return user;
	}
	
	

}
