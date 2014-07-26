package net.cloudengine.web.crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.cloudengine.dao.mongodb.impl.MongoPage;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.web.crud.support.CrudInterface;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

@RunWith(Parameterized.class)
public class CrudControllerTest<E, PK extends Serializable> {
	
	private static RepositoryLocator repositoryLocator;
	private CrudInterface<E, Serializable> crudToTest;
	private Serializable pk;
	
	public CrudControllerTest(CrudInterface<E, Serializable> crudToTest, Serializable pk) {
		super();
		this.crudToTest = crudToTest;
		this.pk = pk;
	}

	@SuppressWarnings("unchecked")
	@Parameters
	public static Collection<Object[]> data() {
		Repository repo = Mockito.mock(Repository.class);
		Mockito.when(repo.list(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new MongoPage(new ArrayList(), 1, 1, 1));

		RepositoryLocator repositoryLocator = Mockito.mock(RepositoryLocator.class);
		Mockito.when(repositoryLocator.getRepository(Mockito.any(Class.class))).thenReturn(repo);

		Object[][] data = {
			{new GroupCrudController(repositoryLocator), new ObjectId()},
			{new RoleCrudController(repositoryLocator), new ObjectId()},
		};
		return Arrays.asList(data);
	}

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test_list() {
		ModelAndView mav = crudToTest.list();
		Assert.assertTrue(mav.getViewName().startsWith("redirect"));
	}
	
	@Test
	public void test_listWithPageAndSize() {
		ModelAndView mav = crudToTest.list(1,10);
		Assert.assertNotNull(mav.getModelMap().get("entities"));
		Assert.assertNotNull(mav.getModelMap().get("gridModel"));
		Assert.assertEquals("/crud/list", mav.getViewName());
	}
	
	@Test
	public void test_delete() {
		ModelAndView mav = crudToTest.delete(pk);
		Assert.assertTrue(mav.getViewName().startsWith("redirect"));
	}

}
