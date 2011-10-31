package net.rescueapp.utilities;

import java.util.concurrent.atomic.AtomicLong;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoApp {

	public static void main(String[] args) throws Exception {

		final AtomicLong number = new AtomicLong(0L);
		final Mongo mongo = new Mongo();
		
		Runnable run = new Runnable() {

			@Override
			public void run() {

				try {
					
					DB db = mongo.getDB("stress");
					DBCollection collection = db.getCollection("bigtable");

					for (int i = 0; i < 700000; i++) {
						
						if ((i % 10) == 0)
							Thread.sleep(5);
						
						BasicDBObject doc = new BasicDBObject();
						doc.put("number", number.addAndGet(1));

						collection.insert(doc);

						if ((i % 100000) == 0) {
							System.out.println("Insertados: " + i);
						}

					}
				} catch (Exception e) {
				}

			}
		};
		
		Thread []t = new Thread[20];
		for (int j = 0; j<10;j++) {
			t[j] = new Thread(run);
			t[j].start();			
		}
		
		for (int j = 0; j<10;j++) {
			t[j].join();			
		}
	}

}
