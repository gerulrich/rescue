package net.cloudengine.web.statistics;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.cloudengine.rest.model.statistics.Tuple;

import org.bson.BasicBSONObject;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

public class HistogramMapReduce {
	
	public List<Tuple<String,Double>> topMethods(DB db, String colName , String field, TimeUnits time) {
		List<Tuple<String,Double>> result = Lists.newArrayList();
		
		String map = "function Map() { emit(this.controller+'.'+this.method, 1); }";
		String reduce = "function Reduce(key, values) { return Array.sum(values); }";
		
		Calendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.HOUR, -time.getHoursRange());
		BasicDBObject query = new BasicDBObject();
		query.put(field, BasicDBObjectBuilder.start("$gte", gc.getTime()).add("$lte", new Date()).get());
		
		DBCollection col = db.getCollection(colName);
		MapReduceCommand command = new MapReduceCommand(col, map, reduce, null, MapReduceCommand.OutputType.INLINE, query);
		
		MapReduceOutput out = col.mapReduce(command);
		
		double total = 0;
		for (DBObject ro : out.results()) {
			BasicBSONObject object = (BasicBSONObject)ro; 
			String name = object.getString("_id");
			Double y = object.getDouble("value");
			result.add(new Tuple<String,Double>(name, y));
			total+=y;
		}
		return sortAndTop(result, total);
	}
	
	public List<Tuple<String,Double>> topErrorMethods(DB db, String colName , String field, TimeUnits time) {
		List<Tuple<String,Double>> result = Lists.newArrayList();
		
		String map = "function Map() { var val = 0; if (this.status == 'ERROR') val = 1; emit(this.controller+'.'+this.method, {error: val, count: 1})}";
		String reduce = "function Reduce(key, values) { " +
			"var err = 0, count = 0;"+
			"for (var i = 0; i < values.length; i++) {"+
	    	"  err+= values[i].error;"+
			"  count+=values[i].count;"+
			"}"+
			"return {error: err, count: count};"+
		"}";
		String finalize = "function Finalize(key, reduced) { return reduced.error / reduced.count; }";
		
		Calendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.HOUR, -time.getHoursRange());
		BasicDBObject query = new BasicDBObject();
		query.put(field, BasicDBObjectBuilder.start("$gte", gc.getTime()).add("$lte", new Date()).get());
		
		DBCollection col = db.getCollection(colName);
		MapReduceCommand command = new MapReduceCommand(col, map, reduce, null, MapReduceCommand.OutputType.INLINE, query);
		command.setFinalize(finalize);
		
		MapReduceOutput out = col.mapReduce(command);
		
		for (DBObject ro : out.results()) {
			BasicBSONObject object = (BasicBSONObject)ro; 
			String name = object.getString("_id");
			Double y = object.getDouble("value");
			if (y > 0) {
				result.add(new Tuple<String,Double>(name, y*100));
			}
		}
		return result;
	}
	
	private List<Tuple<String,Double>> sortAndTop(List<Tuple<String,Double>> list, double total) {
		Collections.sort(list, new Comparator<Tuple<String,Double>>() {
			@Override
			public int compare(Tuple<String, Double> o1, Tuple<String, Double> o2) {
				int val = o2.getY().compareTo(o1.getY());
				if (val == 0) {
					return o2.getX().compareTo(o1.getX()); 
				}
				return val;
			}
		});
		return toPercentage(list, total);
	}
	
	private List<Tuple<String,Double>> toPercentage(List<Tuple<String,Double>> list, double total) {
		List<Tuple<String,Double>> result = Lists.newArrayList();
		double accumulated = 0d;
		for(Tuple<String,Double> tuple : list) {
			tuple.setY(100*tuple.getY()/total);
			result.add(tuple);
			accumulated += tuple.getY();
			if (accumulated > 80) {
				result.add(new Tuple<String,Double>("Otro", 100d - accumulated));
				break;
			}
		}
		return result;
	}
	

}