package net.cloudengine.rpc.mappers;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.cloudengine.util.DateUtil;

import org.junit.Test;
import org.mockito.Mockito;

public class BasicMapperTest extends TestCase {
	
	@Test
	public void test_simpleObjectMapping() {
		DTOMapper mapper = new BasicMapper();
		
		SimpleObject object = new SimpleObject();
		object.setId(10L);
		object.setName("name");
		object.setValue("value");
		object.setProperty("property");
		
		SimpleObjectModel result = mapper.fillModel(object, SimpleObjectModel.class);
		assertNotNull("result cannot be null", result);
		assertEquals(object.getId(), result.getId());
		assertEquals(object.getName(), result.getName());
		assertEquals(object.getProperty(), result.getCustomProperty());
		
	}
	
	@Test
	public void test_simpleObjectMapping_fromDTO() {
		DTOMapper mapper = new BasicMapper();
		
		SimpleObjectModel model = new SimpleObjectModel();
		model.setId(10L);
		model.setName("name");
		model.setValue("value");
		model.setCustomProperty("property");
		
		SimpleObject result = mapper.fillEntity(model, SimpleObject.class);
		assertNotNull("result cannot be null", result);
		assertEquals(model.getId(), result.getId());
		assertEquals(model.getName(), result.getName());
		assertEquals(model.getCustomProperty(), result.getProperty());
	}	
	
	@Test
	public void test_parentChildFlatten() {
		DTOMapper mapper = new BasicMapper();
		
		ChildObject child = new ChildObject();
		child.setChildName("child name");
		child.setChildNumber(44);
		child.setDate(DateUtil.convertToDate("01/01/2000"));
		
		ParentObject parent = new ParentObject();
		parent.setEnabled(true);
		parent.setChild(child);
		
		ParentChildFlatModel result = mapper.fillModel(parent, ParentChildFlatModel.class);
		assertNotNull("result cannot be null", result);
		assertEquals(parent.isEnabled(), result.getEnabled().booleanValue());
		assertEquals(child.getChildName(), result.getName());
		assertEquals(child.getChildNumber(), result.getNumber());
		assertEquals("01/01/2000 00:00:00", result.getDate());
	}
	
	@Test
	public void test_parentChildFlatten_fromDTO() {
		DTOMapper mapper = new BasicMapper();
		
		ParentChildFlatModel model = new ParentChildFlatModel();
		model.setName("child name");
		model.setNumber(44);
		model.setEnabled(true);
		model.setDate("01/02/2000");
		
		ParentObject result = mapper.fillEntity(model, ParentObject.class);
		assertNotNull("result cannot be null", result);
		assertNotNull("child cannot be null", result.getChild());
		assertEquals(model.getEnabled().booleanValue(), result.isEnabled());
		assertEquals(model.getName(), result.getChild().getChildName());
		assertEquals(model.getNumber(), result.getChild().getChildNumber());
		assertEquals(DateUtil.convertToDate("01/02/2000"), result.getChild().getDate());
	}	
	
	@Test
	public void test_parentChildHierarchy() {
		DTOMapper mapper = new BasicMapper();
		
		ChildObject child = new ChildObject();
		child.setChildName("child name");
		child.setChildNumber(44);
		child.setDate(DateUtil.convertToDate("01/01/2000"));
		
		ParentObject parent = new ParentObject();
		parent.setEnabled(true);
		parent.setChild(child);
		
		ParentObjectModel result = mapper.fillModel(parent, ParentObjectModel.class);
		assertNotNull("result cannot be null", result);
		assertEquals(parent.isEnabled(), result.isEnabled());
		assertEquals(child.getChildName(), result.getChild().getChildName());
		assertEquals(child.getChildNumber(), result.getChild().getChildNumber());
		assertEquals("01/01/2000 00:00:00", result.getChild().getDate());
	}
	
	@Test
	public void test_parentChildHierarchy_fromDTO() {
		DTOMapper mapper = new BasicMapper();
		
		ChildObjectModel child = new ChildObjectModel();
		child.setChildName("child name");
		child.setChildNumber(44);
		child.setDate("01/02/2000");
		
		ParentObjectModel model = new ParentObjectModel();
		model.setEnabled(true);
		model.setChild(child);
		
		ParentObject result = mapper.fillEntity(model, ParentObject.class);
		assertNotNull("result cannot be null", result);
		assertNotNull("result child cannot be null", result.getChild());
		assertEquals(model.isEnabled(), result.isEnabled());
		assertEquals(child.getChildName(), result.getChild().getChildName());
		assertEquals(child.getChildNumber(), result.getChild().getChildNumber());
		assertEquals(DateUtil.convertToDate(child.getDate()), result.getChild().getDate());
	}
	
	@Test
	public void test_fillSimpleEntityShouldNotOverrideEqualsProperty() {
		DTOMapper mapper = new BasicMapper();
		
		SimpleObjectModel model = new SimpleObjectModel();
		model.setId(10L);
		model.setName("name");
		model.setValue("value");
		model.setCustomProperty("property");
		
		SimpleObject entity = Mockito.mock(SimpleObject.class);
		Mockito.when(entity.getName()).thenReturn("name");
		Mockito.when(entity.getValue()).thenReturn("value2");
		Mockito.when(entity.getProperty()).thenReturn("prop");
		
		mapper.fillEntity(model, entity);
		
		Mockito.verify(entity).getId();
		Mockito.verify(entity).setId(10L);
		Mockito.verify(entity).getName();
		Mockito.verify(entity).getValue();
		Mockito.verify(entity).setValue("value");
		Mockito.verify(entity).getProperty();
		Mockito.verify(entity).setProperty("property");
		Mockito.verifyNoMoreInteractions(entity);
		
	}
	
	@Test
	public void test_fillEntityShouldNotOverrideEqualsProperty() {
		DTOMapper mapper = new BasicMapper();
		
		ParentObjectModel model = new ParentObjectModel();
		model.setEnabled(true);
		model.setChild(new ChildObjectModel());
		model.getChild().setChildName("child name 1");
		model.getChild().setChildNumber(44);
		
		ParentObject entity = Mockito.mock(ParentObject.class);
		ChildObject child = Mockito.mock(ChildObject.class);
		Mockito.when(entity.getChild()).thenReturn(child);
		Mockito.when(entity.isEnabled()).thenReturn(false);
		Mockito.when(child.getChildName()).thenReturn("name 1");
		Mockito.when(child.getChildNumber()).thenReturn(43);
		
		mapper.fillEntity(model, entity);
		
		Mockito.verify(entity).isEnabled();
		Mockito.verify(entity).setEnabled(true);
		Mockito.verify(entity).getChild();
		Mockito.verifyNoMoreInteractions(entity);

		Mockito.verify(child).getChildName();
		Mockito.verify(child).setChildName("child name 1");
		Mockito.verify(child).getChildNumber();
		Mockito.verify(child).setChildNumber(44);
		Mockito.verify(child).getDate();
		Mockito.verify(child).setDate(null);
		Mockito.verifyNoMoreInteractions(child);
		
	}
	
	public void test_FillEntityCompareWithIdParameter() {
		DTOMapper mapper = new BasicMapper();
		
		BasicEntityModel model = new BasicEntityModel();
		model.setId(10L);
		model.setName("new name");
		model.setComposite(new CompositeEntityModel());
		model.getComposite().setOid(44L);
		model.getComposite().setName("new composite name");
		
		CompositeEntity composite = new CompositeEntity();
		BasicEntity entity = new BasicEntity();
		entity.setId(1L);
		entity.setName("old name");
		entity.setComposite(composite);
		composite.setId(22L);
		composite.setName("old composite name");
		
		mapper.fillEntity(model, entity);
		
		Assert.assertEquals(10L, entity.getId().longValue());
		Assert.assertEquals("new name", entity.getName());
		
		// el composite entity original debe conservar los valores.
		Assert.assertEquals("old composite name", composite.getName());
		Assert.assertEquals(22L, composite.getId().longValue());
		
		// Se reemplaza el composite entity por una instancia nueva con los nuevos valores.
		Assert.assertEquals(44L, entity.getComposite().getId().longValue());
		Assert.assertEquals("new composite name", entity.getComposite().getName());
		
	}
	
	public void test_map_listValue() {
		DTOMapper mapper = new BasicMapper();
		
		ParentWithList entity = new ParentWithList();
		for(int i = 0; i < 2; i++) {
			ListElement le = new ListElement();
			le.setName("name"+i);
			entity.getElements().add(le);
		}
		
		ParentWithListModel model = mapper.fillModel(entity, ParentWithListModel.class);
		
		Assert.assertNotNull(model);
		Assert.assertEquals(2, model.getElements().size());
		Assert.assertEquals("name0", model.getElements().get(0).getName());
		Assert.assertEquals("name1", model.getElements().get(1).getName());
		
		
		
	}
}
