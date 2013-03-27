package net.cloudengine.web.statistics;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

public class HistogramMapReduce {
	
	public GraphSerie<Long, Long> requestStats(DB db, String colName , String field, TimeUnits time) {
		
		Long datett = time.getTimeRepresentation()*1000;
		
		String map = 
			"function Map() {\n"+
				"emit(this."+field+" - (this."+field+" % "+datett+"), {count: 1});\n"+
			"}\n";

		String reduce = 
			"function Reduce(key, values) {\n"+
				"var sum = 0;\n"+
				"values.forEach(function(doc) {\n"+
				"	sum += doc.count;\n"+
				"});\n"+
				"return {count: sum};\n"+
			"}\n";
		
		
		Calendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.HOUR, -time.getHoursRange());
		BasicDBObject query = new BasicDBObject();
		query.put(field, BasicDBObjectBuilder.start("$gte", gc.getTime()).add("$lte", new Date()).get());
		
		DBCollection col = db.getCollection (colName);
		MapReduceCommand command = new MapReduceCommand(col, map, reduce, null, MapReduceCommand.OutputType.INLINE, query);
		
		MapReduceOutput out = col.mapReduce(command);
		GraphSerie<Long, Long> graph = new GraphSerie<Long, Long>("Request");//FIXME
		for (DBObject ro : out.results()) {
			BasicBSONObject object = (BasicBSONObject)ro; 
			Long x = object.getLong("_id");
			Long y = ((BasicBSONObject)object.get("value")).getLong("count");
			graph.addPoint(x, y);
		}
		graph.fillHols(datett);	
		return graph;
	}

}