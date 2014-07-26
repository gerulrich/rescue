package net.cloudengine.web.console;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.cloudengine.model.console.MongoCollection;
import net.cloudengine.service.MongoService;
import net.cloudengine.util.Cast;
import net.cloudengine.util.HexString;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.CommandResult;

public class MongoControllerTest {

	private MongoService service;
//	private MongoDbFactory factory;
//	private DB db;
	
//	private Map<String, DBCollection> collectionNames;
	
	@Before
	public void setUp() throws Exception {
		service = mock(MongoService.class);
		addCollectionsToMock(service);
		
//		collectionNames = new HashMap<String, DBCollection>();
//		collectionNames.put("collection1", mock(DBCollection.class));
//		collectionNames.put("collection2", mock(DBCollection.class));
//		collectionNames.put("collection3", mock(DBCollection.class));
//		collectionNames.put("system.collection4", mock(DBCollection.class));
//		collectionNames.put("fs.collection5", mock(DBCollection.class));
//		collectionNames.put(null, mock(DBCollection.class));
//		
//		factory = mock(MongoDbFactory.class);
//		db = mock(DB.class);
//		when(factory.getDb()).thenReturn(db);
//		when(db.getCollectionNames())
//		 .thenReturn(collectionNames.keySet());
//		
//		when(db.getCollection(Mockito.anyString()))
//			.thenAnswer(new Answer<DBCollection>() {
//				@Override
//				public DBCollection answer(InvocationOnMock invocation) {
//					String collectionName = (String)invocation.getArguments()[0];
//					return collectionNames.get(collectionName);
//				}
//			});
	}

	private void addCollectionsToMock(MongoService service) {
		Object dataArray[][] = {
				//{name,storageSize,size,nindexes,totalindexSize,count}
				{"collection1", 1024L, 1000L, "2", 1300L, 344L},
				{"collection2", 2048L, 1536L, "1", 512L, 1234L}
		};
		
		final List<MongoCollection> collections = new ArrayList<MongoCollection>();
		for(int i = 0; i < dataArray.length; i++) {
			String name = Cast.cast(dataArray[i][0]).asString();
			Long storage = Cast.cast(dataArray[i][1]).asLong();
			Long size = Cast.cast(dataArray[i][2]).asLong();
			String nindex = Cast.cast(dataArray[i][3]).asString();
			Long indexSize = Cast.cast(dataArray[i][4]).asLong();
			Long count = Cast.cast(dataArray[i][5]).asLong();
			CommandResult result = Mockito.mock(CommandResult.class);
			when(result.getLong(Mockito.eq("storageSize"))).thenReturn(storage);
			when(result.getLong(Mockito.eq("size"))).thenReturn(size);
			when(result.getString(Mockito.eq("nindexes"))).thenReturn(nindex);
			when(result.getLong(Mockito.eq("totalIndexSize"))).thenReturn(indexSize);
			when(result.getLong(Mockito.eq("count"))).thenReturn(count);
			MongoCollection collection = new MongoCollection(name, result);
			collections.add(collection);
		}
		when(service.getCollections()).thenReturn(collections);
		when(service.isValidCollection(Mockito.anyString())).thenAnswer(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				boolean isValid = false;
				for(MongoCollection collection : collections) {
					if (collection.getName().equals(invocation.getArguments()[0])) {
						isValid = true;
						break;
					}
				}
				return isValid; 
			}
		});
	}

	@Test
	@Ignore
	public void test_mongoCollections_shouldReturnCollections() {
		MongoController controller = new MongoController(service);
		ModelAndView mav = controller.mongoCollections();
		List<MongoCollection> listCollection = Cast.cast(mav.getModelMap().get("collections")).asList(MongoCollection.class);
		Assert.assertEquals(2, listCollection.size());
		
		MongoCollection collection1 = listCollection.get(0); 
		validateCollectionName(collection1, "collection1");
		validateStorage(collection1, "1,0 KB");
		validateSize(collection1, "1000 B");
		validateIndexes(collection1, "2");
		validateIndexSize(collection1, "1,3 KB");
		validateCount(collection1, 344L);
		
		MongoCollection collection2 = listCollection.get(1);
		validateCollectionName(collection2, "collection2");
		validateStorage(collection2, "2,0 KB");
		validateSize(collection2, "1,5 KB");
		validateIndexes(collection2, "1");	
		validateIndexSize(collection2, "512 B");
		validateCount(collection2, 1234L);
		
		Mockito.verify(service, Mockito.times(1)).getCollections();
		Mockito.verifyNoMoreInteractions(service);
	}
	
	@Test
	@Ignore
	public void test_dropCollection_shouldDropAndRedirect() {
		String collectionToDrop = "Collection1";
		MongoController controller = new MongoController(service);
		ModelAndView mav = controller.dropCollection(HexString.encode(collectionToDrop));
		
		Mockito.verify(service, Mockito.times(1)).drop(collectionToDrop);
		Mockito.verifyNoMoreInteractions(service);
		Assert.assertTrue(mav.getViewName().startsWith("redirect"));
	}
	
	@Test
	@Ignore
	public void test_showCollection_withoutPagingData_shouldRedirect() {
		String collectionToShow = "collection1";
		MongoController controller = new MongoController(service);
		
		ModelAndView mav = controller.showCollection(HexString.encode(collectionToShow));
		Assert.assertTrue(mav.getViewName().startsWith("redirect"));
		Mockito.verifyZeroInteractions(service);
		
	}
	
	@Test
	@Ignore
	public void test_showCollection_withPagingData_shouldReturnPageResult() {
		String collectionToShow = "collection1";
		int page = 3;
		int size = 15;
		
		MongoController controller = new MongoController(service);
		controller.showCollection(HexString.encode(collectionToShow), page, size);
		Mockito.verify(service, Mockito.times(1)).isValidCollection(collectionToShow);
		Mockito.verify(service, Mockito.times(1)).getHeaders(collectionToShow, page, size);
		Mockito.verify(service, Mockito.times(1)).getObjects(collectionToShow, page, size);
		Mockito.verifyNoMoreInteractions(service);
		
	}
	
	@Test
	@Ignore
	public void test_showCollection_withInvalidCollectionPagingData_shouldReturnPageResult() {
		String collectionToShow = "fs.collection1";
		int page = 3;
		int size = 15;
		
		MongoController controller = new MongoController(service);
		ModelAndView mav = controller.showCollection(HexString.encode(collectionToShow), page, size);
		Assert.assertTrue(mav.getViewName().startsWith("redirect"));
		Mockito.verify(service, Mockito.times(1)).isValidCollection(collectionToShow);
		Mockito.verifyNoMoreInteractions(service);
		
	}
	
	private void validateCollectionName(MongoCollection mongoCollection, String name) {
		Assert.assertEquals(name, mongoCollection.getName());
		Assert.assertEquals(HexString.encode(name), mongoCollection.getEncodedName());
	}
	
	private void validateStorage(MongoCollection mongoCollection, String storage) {
		Assert.assertEquals(storage, mongoCollection.getStorage());
	}
	
	private void validateSize(MongoCollection mongoCollection, String size) {
		Assert.assertEquals(size, mongoCollection.getSize());
	}
	
	private void validateIndexes(MongoCollection mongoCollection, String indexes) {
		Assert.assertEquals(indexes, mongoCollection.getIndexes());
	}
	
	private void validateIndexSize(MongoCollection mongoCollection, String indexSize) {
		Assert.assertEquals(indexSize, mongoCollection.getIndexSize());
	}
	
	private void validateCount(MongoCollection mongoCollection, Long count) {
		Assert.assertEquals(count.longValue(), mongoCollection.getCount());
	}

}
