package net.cloudengine.web.statistics;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.cloudengine.api.mongo.MongoDBWrapper;
import net.cloudengine.model.statistics.MapReduceUtil;
import net.cloudengine.rest.model.statistics.GraphSerie;

import org.bson.BasicBSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

@Service
public class HistogramServiceImpl implements HistogramService {

//	private static final String FUNCTION_REDUCE_AVG = 
//		"function Reduce(key, values) { " +
//			"var sum = 0, count = 0;"+ 
//			"for (var i = 0; i < values.length; i++) {"+
//	    	"  sum+= values[i].time;"+
//			"  count+=values[i].count;"+
//			"}"+
//			"return {time: sum, count: count};"+
//		"}";
	private static final String FUNCTION_MAP_VALUE = "function Map() { emit(this.%s - (this.%s %% %s), {time:this.executionTime, count:1});}";
	private static final String FINALIZE_FUNC = "function Finalize(key, reduced) { return reduced.time / reduced.count; }";
	private SimpleMongoDbFactory factory;
	
	@Autowired
	public HistogramServiceImpl(MongoDBWrapper mongoDBWrapper) {
		super();
		this.factory = mongoDBWrapper.getFactory();
	}
	
	@Override
	public GraphSerie<Long, Long> count(String timestampColumn, String collection, TimeUnits time) {
		Long datett = time.getTimeRepresentation()*1000;
		
		String map = String.format("function Map() { emit(this.%s - (this.%s %% %s), 1);}", timestampColumn, timestampColumn, datett);
		String reduce = "function Reduce(key, values) { return Array.sum(values);}";
		
		Calendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.HOUR, -time.getHoursRange());
		BasicDBObject query = new BasicDBObject();
		query.put(timestampColumn, BasicDBObjectBuilder.start("$gte", gc.getTime()).add("$lte", new Date()).get());
		
		DBCollection col = factory.getDb().getCollection (collection);
		MapReduceCommand command = new MapReduceCommand(col, map, reduce, null, MapReduceCommand.OutputType.INLINE, query);
		
		MapReduceOutput out = col.mapReduce(command);
		GraphSerie<Long, Long> graph = new GraphSerie<Long, Long>("Request x minuto", gc.getTimeInMillis(), System.currentTimeMillis());
		for (DBObject ro : out.results()) {
			BasicBSONObject object = (BasicBSONObject)ro; 
			Long x = object.getLong("_id");
			Long y = object.getLong("value");
			graph.addPoint(x, y);
		}
		graph.fillHols(datett);	
		return graph;
	}

	@Override
	public GraphSerie<Long, Double> avg(String timestampColumn, String valueColumn, String collection, TimeUnits time) {
		Long datett = time.getTimeRepresentation()*1000;
		
		String map = String.format(FUNCTION_MAP_VALUE, timestampColumn, timestampColumn, datett);
		String reduce = MapReduceUtil.sumAndCountReduceFunction("time", "count");
		String finalize = MapReduceUtil.divideFinalizeFunction("time", "count");
		
		Calendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.HOUR, -time.getHoursRange());
		BasicDBObject query = new BasicDBObject();
		query.put(timestampColumn, BasicDBObjectBuilder.start("$gte", gc.getTime()).add("$lte", new Date()).get());
		
		DBCollection col = factory.getDb().getCollection (collection);
		MapReduceCommand command = new MapReduceCommand(col, map, reduce, null, MapReduceCommand.OutputType.INLINE, query);
		command.setFinalize(finalize);
		
		MapReduceOutput out = col.mapReduce(command);
		GraphSerie<Long, Double> graph = new GraphSerie<Long, Double>("Request", gc.getTimeInMillis(), System.currentTimeMillis());
		for (DBObject ro : out.results()) {
			BasicBSONObject object = (BasicBSONObject)ro; 
			Long x = object.getLong("_id");
			Double y = object.getDouble("value");
			graph.addPoint(x, y);
		}
		graph.fillHols(datett);	
		return graph;
	}

}
