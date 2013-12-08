package net.cloudengine.api.mongo.dao;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.cloudengine.rest.model.statistics.Tuple;
import net.cloudengine.web.statistics.TimeUnits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;

@Service
public class MongoMapReduceService implements MapReduceService {

	private static final String COLLECTION = "request_log";
	private static final String TIME_FIELD = "time";
	
	
	private static final String SUM_FUNCTION = "function Reduce(key, values) { return Array.sum(values); }"; 

	private MongoOperations mongoOperations;

	@Autowired
	public MongoMapReduceService(MongoOperations mongoOperations) {
		super();
		this.mongoOperations = mongoOperations;
	}
	
	@Override
	public List<Tuple<String,Double>> topMethods(TimeUnits time) {
		List<Tuple<String,Double>> result = Lists.newArrayList();
		
		String map = "function Map() { emit(this.controller+'.'+this.method, 1); }";
		
		Calendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.HOUR, -time.getHoursRange());
		BasicDBObject query = new BasicDBObject();
		query.put(TIME_FIELD, BasicDBObjectBuilder.start("$gte", gc.getTime()).add("$lte", new Date()).get());
		
		MapReduceResults<ValueObject> results = mongoOperations.mapReduce(COLLECTION, map, SUM_FUNCTION, ValueObject.class);
		double total = 0;
		for (ValueObject valueObject : results) {
			  result.add(new Tuple<String,Double>(valueObject.getId(), valueObject.getValue()));			  
			  total+=valueObject.getValue();
		} 
		
		
		return sortAndTop(result, total);
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
	
	public class ValueObject {

		private String id;

		private double value;
			
		public String getId() {
			return id;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "ValueObject [id=" + id + ", value=" + value + "]";
		}
			
	}

}
