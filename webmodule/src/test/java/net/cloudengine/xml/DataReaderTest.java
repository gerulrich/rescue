package net.cloudengine.xml;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DataReaderTest {

	private DBCollection createDBCollectionMock(Long count) {
		DBCollection collection = Mockito.mock(DBCollection.class);
		Mockito
			.when(collection.count(Mockito.any(DBObject.class)))
			.thenReturn(count);

		Mockito
			.when(collection.findOne(Mockito.any(DBObject.class)))
			.thenAnswer(new Answer<DBObject>() {
				@Override
				public DBObject answer(InvocationOnMock invocation) throws Throwable {
					return (DBObject) invocation.getArguments()[0];
				}
			});
		return collection;
	}
	
	
	@Test
	public void testLoadAppData_previouslySaveData() {
		DBCollection collection = createDBCollectionMock(1L);
		
		DB db = Mockito.mock(DB.class);
		Mockito
			.when(db.getCollection(Mockito.anyString()))
			.thenReturn(collection);
		
		
		new DataReader().loadAppData(db, "/net/cloudengine/xml/testdata.xml");
		
		Mockito.verify(db, Mockito.times(1)).getCollection("user");
		Mockito.verify(db, Mockito.times(2)).getCollection("permission");
		Mockito.verify(db, Mockito.times(1)).getCollection("role");
		
//		Mockito.verify(collection, Mockito.times(3)).count(Mockito.any(DBObject.class));
		Mockito.verify(collection, Mockito.times(0)).insert(Mockito.any(DBObject.class));
		
	}
	
	@Test
	public void testLoadAppData_saveNewData() {
		DBCollection collection = createDBCollectionMock(0L);
		
		DB db = Mockito.mock(DB.class);
		Mockito
			.when(db.getCollection(Mockito.anyString()))
			.thenReturn(collection);
		
		
		new DataReader().loadAppData(db, "/net/cloudengine/xml/testdata.xml");
		
		Mockito.verify(db, Mockito.times(1)).getCollection("user");
		Mockito.verify(db, Mockito.times(2)).getCollection("permission");
		Mockito.verify(db, Mockito.times(1)).getCollection("role");
		
//		Mockito.verify(collection, Mockito.times(3)).count(Mockito.any(DBObject.class));
		Mockito.verify(collection, Mockito.times(3)).insert(Mockito.any(DBObject.class));
		
	}	

}
